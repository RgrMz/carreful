define(['knockout', 'appController', 'ojs/ojmodule-element-utils', 'accUtils',
	'jquery'], function(ko, app, moduleUtils, accUtils, $) {

		class ProductViewModel {
			constructor() {
				var self = this;

				self.nombre = ko.observable("Detergente");
				self.precio = ko.observable("8,50 €");

				self.productos = ko.observableArray([]);
				self.carrito = ko.observableArray([]);

				self.message = ko.observable(null);
				self.error = ko.observable(null);

				// Header Config
				self.headerConfig = ko.observable({
					'view': [],
					'viewModel': null
				});
				moduleUtils.createView({
					'viewPath': 'views/header.html'
				}).then(function(view) {
					self.headerConfig({
						'view': view,
						'viewModel': app.getHeaderModel()
					})
				})
			}

			add() {
				var self = this;
				let info = {
					nombre: this.nombre(),
					precio: this.precio()
				};
				let data = {
					data: JSON.stringify(info),
					url: "product/add",
					typ: "post",
					contentType: 'application/json',
					success: function(response) {
						self.message("Producto guardado");
						self.getProductos();
					},
					error: function(response) {
						self.error(response.responseJSON.errorMessage);
					}
				};
				$.ajax(data);
			}

			getProductos() {
				let self = this;
				let data = {
					url: "product/getTodos",
					type: "get",
					contentType: 'application/json',
					success: function(response) {
						self.productos(response);
						/* Ya no hace falta lo de abajo porque hemos puesto
						 * en cada botón la llamada a $parent.eliminarProducto(...)  
						 * for (let i=0; i<response.length; i++) {
							let objetito = {
								nombre : response[i].nombre,
								precio : response[i].precio,
								eliminar : function() {
									self.eliminarProducto(response[i].nombre);
								}
							};
							self.productos.push(objetito);
						}*/
					},
					error: function(response) {
						self.error(response.responseJSON.errorMessage);
					}
				};
				$.ajax(data);
			}

			eliminarProducto(nombre) {
				let self = this;
				let data = {
					url: "product/borrarProducto/" + nombre,
					type: "delete",
					contentTyp: 'application/json',
					success: function(response) {
						self.message("Producto eliminado");
						self.getProductos();
					},
					error: function(response) {
						self.error(response.responseJSON.errorMessage);
					}
				};
				$.ajax(data);
			}
			
			connected() {
				accUtils.announce('Menu page loaded.');
				document.title = "Menu";
				this.getProductos();
			};

			disconnected() {
				// Implement if needed
			};

			transitionCompleted() {
				// Implement if needed
			};
		}

		return ProductViewModel;
	});

