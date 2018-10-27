package com.andrecarlos.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andrecarlos.cursomc.domain.ItemPedido;
import com.andrecarlos.cursomc.domain.PagamentoComBoleto;
import com.andrecarlos.cursomc.domain.Pedido;
import com.andrecarlos.cursomc.domain.enums.EstadoPagamento;
import com.andrecarlos.cursomc.repositories.ItemPedidoRepository;
import com.andrecarlos.cursomc.repositories.PagamentoRepository;
import com.andrecarlos.cursomc.repositories.PedidoRepository;
import com.andrecarlos.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired // Quer dizer q ela será instanciada automaticamente pelo spring
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;

	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}

	@Transactional
	public Pedido insert(Pedido obj) {
		
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		// Associando o pedido ao pagamento
		obj.getPagamento().setPedido(obj);
		
		// Se o pagamento for do tipo PagamentoComBoleto
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		
		obj = repo.save(obj);
		// Salvando o pagamento
		pagamentoRepository.save(obj.getPagamento());
		
		for (ItemPedido ip: obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		// Salvando o ItemPedido
		itemPedidoRepository.saveAll(obj.getItens());
		// Para enviar email de confirmação
//		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
}
