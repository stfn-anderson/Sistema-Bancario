package com.example.sistemaBanco.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sistemaBanco.dto.GetConta;
import com.example.sistemaBanco.entities.Conta;
import com.example.sistemaBanco.repository.ContaRepository;
import com.example.sistemaBanco.service.exceptions.ContaNotFoundException;



@Service
public class ContaService {

	@Autowired
	private ContaRepository contaRepository;
	

	// pegar todas as contas
	public List<GetConta> findAll() {
		List<Conta> contas = contaRepository.findAll();
		List<GetConta> getConta = new ArrayList<>(); // uma lisra vazia que armazena os obj convertdos

		for (Conta conta : contas) { // pecorre cada obj contas
			getConta.add(GetConta.fromConta(conta)); // pega a lista e adiciona os usuarios convertidos
		}
		return getConta;
	}

	// para pegar a conta por id
	public GetConta findById(Long id) {
		Optional<Conta> optionalConta = contaRepository.findById(id);
		// ver se encontrou
		Conta conta = optionalConta.orElseThrow(() -> new ContaNotFoundException(id)); // se não gera esse erro
		// mapeando a conta para o get
		GetConta getConta = GetConta.fromConta(conta);
		return getConta;
	}

	// para atualizar a conta
	public void atualizarConta(Conta conta) {
		contaRepository.save(conta);
	}

//	// deletar a conta
//	public void deletarConta(Long id) {
//	    // vai ver se existe
//	    if (!contaRepository.existsById(id)) {
//	        throw new ContaNotFoundException(id); // gera o erro caso não exista
//	    }  
//	    // pegar todas as transferencias da contsa
//	    List<Transferencia> transferencias = transferenciaRepository.findByContaOrigemContaDestino(id, id);
//	    //apagar tds as transferencias
//	    transferenciaRepository.deleteAll(transferencias);
//	    // deleta a conta
//	    contaRepository.deleteById(id);
//	}
}
