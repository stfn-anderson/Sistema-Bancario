package com.example.sistemaBanco.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;

import com.example.sistemaBanco.dto.IdDto;
import com.example.sistemaBanco.entities.Transacao;
import com.example.sistemaBanco.entities.enums.TipoTransacao;

public class PostTransacaoTransferencia implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idContaOrigem;
	private Long idContaDestino;
	private BigDecimal valor;

	public PostTransacaoTransferencia() {
	}

	public PostTransacaoTransferencia(Long idContaOrigem, Long idContaDestino, BigDecimal valor) {
		super();
		this.idContaOrigem = idContaOrigem;
		this.idContaDestino = idContaDestino;
		this.valor = valor;
	}

	public Transacao toTransacao() { 
        return new Transacao(
            null, 
            new IdDto(idContaOrigem).toConta(), 
            new IdDto(idContaDestino).toConta(), 
            valor, 
            null, 
            TipoTransacao.TRANSFERENCIA
        );
    }

	public static PostTransacaoTransferencia fromTransacaoTransferencia(Transacao transacao) { 
        return new PostTransacaoTransferencia(
            transacao.getContaOrigem() != null ? transacao.getContaOrigem().getId() : null,
            transacao.getContaDestino() != null ? transacao.getContaDestino().getId() : null,
            transacao.getValor()
        );
    }

	public Long getIdContaOrigem() {
		return idContaOrigem;
	}

	public void setIdContaOrigem(Long idContaOrigem) {
		this.idContaOrigem = idContaOrigem;
	}

	public Long getIdContaDestino() {
		return idContaDestino;
	}

	public void setIdContaDestino(Long idContaDestino) {
		this.idContaDestino = idContaDestino;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}
