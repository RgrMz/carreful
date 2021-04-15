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
				
				self.categorias = ko.observableArray([]);

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

			eliminarDelCarrito(nombre) {
				let self = this;
				let data = {
					url: "product/eliminarDelCarrito/" + nombre,
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

			getImporte() {
				let self = this;
				let data = {
					url: "product/getImporte",
					type: "get",
					contentTyp: 'application/json',
					success: function(response) {
						self.importe(response.toString() + ' €');
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
						for (let i=0; i<response.length; i++) {
							let objetito = {
								name : response[i].nombre,
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
			
			getProductosPorCategoria(nombre){
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

			register() {
				app.router.go({ path: "register" });
			}

			connected() {
				accUtils.announce('Productos page loaded.');
				document.title = "Productos";
				this.getProductos();
				this.getCategorias();
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
