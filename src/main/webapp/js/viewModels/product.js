define(['knockout', 'appController', 'ojs/ojmodule-element-utils', 'accUtils',
	'jquery'], function(ko, app, moduleUtils, accUtils, $) {

		class ProductViewModel {
			constructor() {
				var self = this;

				self.nombre = ko.observable("Detergente");
				self.precio = ko.observable("8,50 €");
				self.picture = ko.observable("");
				
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

			addAlCarrito(nombre) {
				let self = this;
				let data = {
					url: "product/addAlCarrito/" + nombre,
					type: "post",
					contentTyp: 'application/json',
					success: function(response) {
						self.message("Producto añadido al carrito");
						self.carrito(response.products);
					},
					error: function(response) {
						self.error(response.responseJSON.errorMessage);
					}
				};
				$.ajax(data);
			}

			register() {
				app.router.go({ path: "register" });
			}

			connected() {
				accUtils.announce('Login page loaded.');
				document.title = "Login";
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
