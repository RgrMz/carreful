define(['knockout', 'appController', 'ojs/ojmodule-element-utils', 'accUtils',
	'jquery'], function(ko, app, moduleUtils, accUtils, $) {

		class ProductViewModel {
			constructor() {
				var self = this;

				self.nombre = ko.observable("");
				self.precio = ko.observable("");
				self.imagenBuscada = ko.observable("https://image.flaticon.com/icons/png/512/18/18436.png");

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
					precio: this.precio(),
					congelado: document.getElementById('congelado').checked
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

			buscarImagen() {
				let self = this;
				$.getJSON("http://api.flickr.com/services/feeds/photos_public.gne?jsoncallback=?",
					{
						tags: self.nombre,
						tagmode: "any",
						format: "json"
					},
					function(data) {
						var rnd = Math.floor(Math.random() * 20);

						var image_src = data.items[rnd]['media']['m'].replace("_m", "_b");

						self.imagenBuscada(image_src);
					});
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

