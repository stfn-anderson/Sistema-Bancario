package com.example.sistemaBanco.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sistemaBanco.dto.GetTransacao;
import com.example.sistemaBanco.entities.Conta;
import com.example.sistemaBanco.entities.Transacao;
import com.example.sistemaBanco.entities.Usuario;
import com.example.sistemaBanco.entities.enums.TipoTransacao;
import com.example.sistemaBanco.entities.enums.UsuarioTipo;
import com.example.sistemaBanco.repository.ContaRepository;
import com.example.sistemaBanco.repository.TransacaoRepository;
import com.example.sistemaBanco.service.exceptions.ContaDestinoException;
import com.example.sistemaBanco.service.exceptions.ContaOrigemException;
import com.example.sistemaBanco.service.exceptions.ContasIguaisException;
import com.example.sistemaBanco.service.exceptions.SaldoInsuficienteException;
import com.example.sistemaBanco.service.exceptions.TransferenciaNotFoundException;
import com.example.sistemaBanco.service.exceptions.ValorInvalidoException;

import jakarta.transaction.Transactional;

@Service
public class TransacaoService {

	@Autowired
	private TransacaoRepository transacaoRepository;

	@Autowired
	private ContaRepository contaRepository;

	public List<GetTransacao> findAll() {
		List<Transacao> transferencias = transacaoRepository.findAll(); // pego a lista de usuarios
		List<GetTransacao> getTransferencias = new ArrayList<>(); // uma lisra vazia que armazena os obj convertdos

		for (Transacao transferencia : transferencias) { // pecorre cada obj de usuarios
			getTransferencias.add(GetTransacao.fromTransferencia(transferencia)); // pega a lista e adiciona os
																					// usuarios convertidos
		}

		return getTransferencias;
	}

	// para pegar a transferencia por id
	public GetTransacao findById(Long id) {
		Optional<Transacao> optionalTransferencia = transacaoRepository.findById(id);
		// ver se encontrou
		Transacao transferencia = optionalTransferencia.orElseThrow(() -> new TransferenciaNotFoundException(id)); // se n, gera esse erro 
		// mapeando a transferencia para o get
		GetTransacao getTransferencia = GetTransacao.fromTransferencia(transferencia);
		return getTransferencia;
	}


	@Transactional
	public void realizarSaque(Transacao saque) {
		Conta contaOrigem = contaRepository.findById(saque.getContaOrigem().getId()) // vendo se a conta origem existe
				.orElseThrow(() -> new ContaOrigemException("Conta de origem não encontrada.")); // se n gera esse erro 

		if (saque.getValor() <= 0) {  // se o valoe do saque for menor ou = a zero lança esse erro 
			throw new ValorInvalidoException("O valor da transação deve ser positivo.");
		}
		// se o saldo da conta for maior qu eo valor do saque ai faz a operação 
		if (contaOrigem.getSaldo() >= saque.getValor()) {
			contaOrigem.setSaldo(contaOrigem.getSaldo() - saque.getValor()); // pega o saldo e subtrai do valor sacado 
			contaRepository.save(contaOrigem); // salva a conta 
			
			//saque.setTipo("SAQUE");
			Date dataAtual = new Date();
	        saque.setData(dataAtual);
	        
			transacaoRepository.save(saque); // salva a transacao 
		} 
		else {
			throw new SaldoInsuficienteException("Saldo insuficiente para realizar a transação."); // se n, gera esse erro 
		}
	}

	@Transactional
	public void realizarDeposito(Transacao deposito) {
		
		// o deposito é apenas enviar o dinheiro para uma conta ent n tem conta origem 

		Conta contaDestino = contaRepository.findById(deposito.getContaDestino().getId())  // v se a conta que vai ser depositada existe 
				.orElseThrow(() -> new ContaDestinoException("Conta de destino não encontrada.")); // se n encontrar gera esse erro

		if (deposito.getValor() <= 0) { // vendo se o valor do deposito é positivo
			throw new ValorInvalidoException("O valor do depósito deve ser um valor positivo.");
		}
		
//		// vendo se o usuario é lojista 
//	    if (deposito.getContaOrigem().getUsuario().getTipo().equals(UsuarioTipo.LOJISTA)) {
//	        throw new UsuarioTipoException("Lojistas não podem realizar depósitos.");
//	    }
	    
		contaDestino.setSaldo(contaDestino.getSaldo() + deposito.getValor());  // se for peha a conta destino e soma com o novo valor depositado
		contaRepository.save(contaDestino);
		
		Date dataAtual = new Date();
        deposito.setData(dataAtual);
        
		transacaoRepository.save(deposito);
	}
	
	@Transactional
	public void realizarTransferencia(Transacao transferencia) {
		if (transferencia.getContaOrigem().equals(transferencia.getContaDestino())) {
	        throw new ContasIguaisException("A conta de origem é igual à conta de destino");
	    }
	    try {
	        realizarSaque(transferencia); // chamando metodo sauqe
	        realizarDeposito(transferencia); // metodo deposito
	        
	        
	        Date dataAtual = new Date(); 
	        transferencia.setData(dataAtual);
	        
	        // se algum dos erros que tem disponivel aparecer gera a menssagem
	    } catch (ValorInvalidoException | SaldoInsuficienteException | ContaOrigemException | ContaDestinoException e) {
	        throw new RuntimeException("Erro ao realizar a transferência: " + e.getMessage());
	    }
	}

}
