define(['knockout', 'appController', 'ojs/ojmodule-element-utils', 'accUtils',
	'jquery', "ojs/ojfilmstrip"], function(ko, app, moduleUtils, accUtils, $) {

		class ProductViewModel {
			constructor() {
				var self = this;

				self.nombre = ko.observable("Detergente");
				self.precio = ko.observable("8,50 €");
				self.picture = ko.observable("");
				self.importe = ko.observable();

				self.productos = ko.observableArray([]);
				self.carrito = ko.observableArray([]);
				self.categorias = ko.observableArray([]);

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
						self.getImporte();
					},
					error: function(response) {
						self.error(response.responseJSON.errorMessage);
					}
				};
				$.ajax(data);
			}

			eliminarProductoDelCarrito(nombre) {
				let self = this;
				let data = {
					url: "product/eliminarProductoDelCarrito/" + nombre,
					type: "post",
					contentTyp: 'application/json',
					success: function(response) {
						self.message("Quitaste el producto: " + nombre + " del carrito");
						document.getElementById("msg").style.color = "red";
						self.carrito(response.products);
						self.getImporte();
					},
					error: function(response) {
						self.error(response.responseJSON.errorMessage);
					}
				};
				$.ajax(data);
			}

			eliminarUnidadDelCarrito(nombre) {
				let self = this;
				let data = {
					url: "product/eliminarUnidadDelCarrito/" + nombre,
					type: "post",
					contentTyp: 'application/json',
					success: function(response) {
						self.message("Quitaste 1 unidad del producto: " + nombre);
						document.getElementById("msg").style.color = "red";
						self.carrito(response.products);
						self.getImporte();
					},
					error: function(response) {
						self.error(response.responseJSON.errorMessage);
					}
				};
				$.ajax(data);
			}

			addUnidadDelCarrito(nombre) {
				let self = this;
				let data = {
					url: "product/addUnidadDelCarrito/" + nombre,
					type: "post",
					contentTyp: 'application/json',
					success: function(response) {
						self.message("Añadiste 1 unidad del producto: " + nombre);
						document.getElementById("msg").style.color = "red";
						self.carrito(response.products);
						self.getImporte();
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

			getCategorias() {
				let self = this;
				let data = {
					url: "categories/getTodas",
					type: "get",
					contentTyp: 'application/json',
					success: function(response) {
						for (let i = 0; i < response.length; i++) {
							let objetito = {
								name: response[i].nombre,
							};
							self.categorias.push(objetito);
						}
					},
					error: function(response) {
						self.error(response.responseJSON.errorMessage);
					}
				};
				$.ajax(data);
			}

			getProductosPorCategoria(nombre) {
				let self = this;
				let data = {
					url: "categories/" + nombre,
					type: "get",
					contentTyp: 'application/json',
					success: function(response) {
						self.productos(response);
					},
					error: function(response) {
						self.error(response.responseJSON.errorMessage);
					}
				};
				$.ajax(data);
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

			register() {
				app.router.go({ path: "register" });
			}

			connected() {
				accUtils.announce('Productos page loaded.');
				document.title = "Productos";
				this.getProductos();
				this.getCategorias();
				this.getCarrito();
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
