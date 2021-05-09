class ViewModelConCarrito {
	
	constructor(ko) {
		this.ko = ko;
		this.carrito = ko.observableArray([]);
		this.importe = ko.observable();
	}
	
	getCarrito() {
		let self = this;
		let data = {
			url: "product/getCarrito",
			type: "get",
			contentTyp: 'application/json',
			success: function(response) {
				if (self.carrito(response.products)) {
					self.carrito(response.products);
					self.getImporte();
				}
			},
			error: function(response) {
				self.error(response.responseJSON.errorMessage);
			}
		};
		$.ajax(data);
	}
	
	vaciarCarrito() {
		let self = this;
		let data = {
			url: "product/vaciarCarrito",
			type: "put",
			contentTyp: 'application/json',
			success: function(response) {
				self.carrito(response.products);
			},
			error: function(response) {
				self.error(response.responseJSON.errorMessage);
			}
		};
		$.ajax(data);
	}
	
	getImporte() {
		let self = this;
		let data = {
			url: "product/getImporte",
			type: "get",
			contentTyp: 'application/json',
			success: function(response) {
				self.importe(response + ' €');
			},
			error: function(response) {
				self.error(response.responseJSON.errorMessage);
			}
		};
		$.ajax(data);
	}

}