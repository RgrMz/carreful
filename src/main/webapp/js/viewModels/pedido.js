define(['knockout', 'appController', 'ojs/ojmodule-element-utils', 'accUtils',
	'jquery', "ojs/ojbootstrap", "ojs/ojknockout", "ojs/ojinputtext", "ojs/ojformlayout", "ojs/ojlabel", "ojs/ojbutton"], function(ko, app, moduleUtils, accUtils, $) {

		class PaymentViewModel{
			constructor() {
				var self = this;

				self.message = ko.observable("");
				self.error = ko.observable("");

				self.nombre = ko.observable();
				self.apellidos = ko.observable();
				self.email = ko.observable();
				self.telefonoMovil = ko.observable();
				self.direccion = ko.observable();
				self.ciudad = ko.observable();
				self.provincia = ko.observable();
				self.codigoPostal = ko.observable();
				self.pais = ko.observable();

				self.gastosEnvio = ko.observable();
				self.precioPedido = ko.observable();

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

			consultarPedido() {
				var self = this;
				const queryString = window.location.search;
				const urlParams = new URLSearchParams(queryString);
				const idPedido = urlParams.get('idPedido')
				var data = {
					url: "pedido/consultarPedido/" + idPedido,
					type: "get",
					contentType: 'application/json',
					success: function(response) {
						self.nombre(response.nombre);
						self.apellidos(response.apellidos);
						self.email(response.email);
						self.telefonoMovil(response.telefonoMovil);
						self.direccion(response.direccion);
						self.ciudad(response.ciudad);
						self.provincia(response.provincia);
						self.codigoPostal(response.codigoPostal);
						self.pais(response.pais);
					},
					error: function(response) {
						self.error(response.responseJSON.errorMessage);
					}
				};
				$.ajax(data);
			}

			connected() {
				accUtils.announce('Información del pedido page loaded.');
				document.title = "Información del pedido";
				this.consultarPedido();
			}


			disconnected() {
				// Implement if needed
			}

			transitionCompleted() {
				// Implement if needed
			}
		}
		return PaymentViewModel;
	});
