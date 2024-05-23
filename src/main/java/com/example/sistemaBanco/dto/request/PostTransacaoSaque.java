package com.example.sistemaBanco.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;

import com.example.sistemaBanco.dto.IdDto;
import com.example.sistemaBanco.entities.Conta;
import com.example.sistemaBanco.entities.Transacao;
import com.example.sistemaBanco.entities.enums.TipoTransacao;

public class PostTransacaoSaque implements Serializable {

	private static final long serialVersionUID = 1L;

	private IdDto conta;
	private BigDecimal valor;

	public PostTransacaoSaque() {
	}

	public PostTransacaoSaque(IdDto conta, BigDecimal valor) {
		super();
		this.conta = conta;
		this.valor = valor;
	}

	public Transacao toTransacao() {
		Conta contaOrigem = new Conta();
		contaOrigem.setId(this.conta.getId());

		Transacao transacao = new Transacao();
		transacao.setContaOrigem(contaOrigem);
		transacao.setValor(this.valor);
		transacao.setTipo(TipoTransacao.SAQUE);

		return transacao;
	}

	public IdDto getConta() {
		return conta;
	}

	public void setConta(IdDto conta) {
		this.conta = conta;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}
