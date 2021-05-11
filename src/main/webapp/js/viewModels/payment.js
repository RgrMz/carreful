define(['knockout', 'appController', 'ojs/ojmodule-element-utils', 'accUtils',
	'jquery', "ojs/ojbootstrap", "ojs/ojknockout", "ojs/ojinputtext", "ojs/ojformlayout", "ojs/ojlabel", "ojs/ojbutton"], function(ko, app, moduleUtils, accUtils, $) {

		class PaymentViewModel extends ViewModelConCarrito {
			constructor() {
				super(ko);
				var self = this;
				// Para paginas que no son single page (index.html con un router que va cambiando la vista) seria asi
				self.stripe = Stripe('pk_test_51Idbt6HmArDnS3pXvdBN6zJ0jyaJS65zY1vMv4z0wfoG3vyjTEoPYMjpGU9G04ZLEUCokpTsvirO806CJ2xN8EwW00kXh8tv4f');

				self.message = ko.observable("");
				self.error = ko.observable("");

				self.nombre = ko.observable("");
				self.apellidos = ko.observable("");
				self.email = ko.observable("");
				self.telefonoMovil = ko.observable("");
				self.direccion = ko.observable("");
				self.ciudad = ko.observable("");
				self.provincia = ko.observable("");
				self.codigoPostal = ko.observable("");
				self.pais = ko.observable("");

				self.tipoPedido = ko.observable("");

				self.hayCongelados = ko.observable(true);
				self.enableRadiosDomicilio = ko.observable(true);
				self.disableAll = ko.observable(true);

				self.gastosEnvio = ko.observable(0);

				self.errorTipoEnvio = ko.observable("");
				self.errorRealizarPedido = ko.observable("");

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

			getGastosDeEnvio() {
				let self = this;
				let tipoPedido = this.tipoPedido();
				var data = {
					url: "pedido/precioGastosEnvio/" + tipoPedido,
					type: "get",
					contentType: 'application/json',
					success: function(response) {
						self.gastosEnvio(response);
						self.getImporteTotal();
					},
					error: function(response) {
						self.error(response.responseJSON.errorMessage);
					}
				};
				$.ajax(data);
			}

			connected() {
				accUtils.announce('Payment page loaded.');
				document.title = "Pago";
				this.getCarrito();
				this.checkCongelados();
			}

			checkCongelados() {
				let self = this;
				var data = {
					url: "pedido/checkCongelados",
					type: "get",
					contentType: 'application/json',
					success: function(response) {
						if (response) {
							alert("Usted añadió al carrito uno o más productos congelados. El tipo de pedido obligatoriamente será \"Domicilio Express\" o \"Recogida\"");
							self.enableRadiosDomicilio(false);
							self.getGastosDeEnvio();
						}
					},
					error: function(response) {
						self.error(response.responseJSON.errorMessage);
					}
				};
				$.ajax(data);
			}

			getImporteTotal() {
				let self = this;
				let data = {
					url: "product/getImporte",
					type: "get",
					contentTyp: 'application/json',
					success: function(response) {
						self.importe((response + self.gastosEnvio()).toFixed(2) + ' €');
					},
					error: function(response) {
						self.error(response.responseJSON.errorMessage);
					}
				};
				$.ajax(data);
			}

			confirmarPedido() {
				let self = this;
				if (self.carrito().length != 0) {
					if (self.nombre() == "" || self.apellidos() == "" || self.email() == "" || self.telefonoMovil() == "" || self.direccion() == "" || self.ciudad() == "" || self.provincia() == "" || self.codigoPostal() == "" || self.pais() == "") {
						self.errorTipoEnvio("Por favor, rellene todos los datos de facturación para poder realizar el pedido.");
					} else {
						if (self.tipoPedido() != "") {
							self.errorTipoEnvio("");
							self.enableRadiosDomicilio(false);
							self.hayCongelados(false);
							self.disableAll(false);
							self.solicitarPreautorizacion();
						} else {
							self.errorTipoEnvio("Para poder confirmar los datos del pedido, por favor, seleccione un tipo de envío.")
						}
					}
				} else {
					self.errorTipoEnvio("El carrito está vacío. Para poder realizar un pedido primero seleccione los productos que desea comprar.");
				}

			}

			solicitarPreautorizacion() {
				var self = this;
				// The items the customer wants to buy
				let info = {
					gastosEnvio: this.gastosEnvio()
				};
				let data = {
					data: JSON.stringify(info),
					url: "payments/solicitarPreautorizacion",
					type: "post",
					contentType: 'application/json',
					success: function(response) {
						// El PaymentController devuelve un String, no un JSON, por eso la response es una String
						self.clientSecret = response;
						self.rellenaFormulario();
					},
					error: function(response) {
						self.message("");
						self.error(response.responseJSON.errorMessage);
					}
				};
				$.ajax(data);
			}

			rellenaFormulario() {
				let self = this;
				var elements = self.stripe.elements();
				var style = {
					base: {
						color: "#32325d",
						fontFamily: 'Arial, sans-serif',
						fontSmoothing: "antialiased",
						fontSize: "16px",
						"::placeholder": {
							color: "#32325d"
						}
					},
					invalid: {
						fontFamily: 'Arial, sans-serif',
						color: "#fa755a",
						iconColor: "#fa755a"
					}
				};

				var card = elements.create("card", { style: style });
				// Stripe injects an iframe into the DOM
				card.mount("#card-element");
				card.on("change", function(event) {
					// Disable the Pay button if there are no card details in the Element
					document.querySelector("#card-error").textContent = event.error ? event.error.message : "";
				});

				var form = document.getElementById("payment-form");
				form.addEventListener("submit", function(event) {
					event.preventDefault();
					// Complete payment when the submit button is clicked
					self.payWithCard(card);
				});
			}

			payWithCard(card) {
				let self = this;
				self.stripe.confirmCardPayment(self.clientSecret, {
					payment_method: {
						card: card
					}
				}).then(function(result) {
					if (result.error) {
						// Show error to your customer (e.g., insufficient funds)
						self.error(result.error.message);
					} else {
						// The payment has been processed!
						if (result.paymentIntent.status === 'succeeded') {
							alert("Pago exitoso");
							self.generarPedido();
						}
					}
				});
			}

			generarPedido() {
				var self = this;
				var info = {
					nombre: this.nombre(),
					apellidos: this.apellidos(),
					email: this.email(),
					telefonoMovil: this.telefonoMovil(),
					direccion: this.direccion(),
					ciudad: this.ciudad(),
					provincia: this.provincia(),
					pais: this.pais(),
					codigoPostal: this.codigoPostal(),
					tipoPedido: this.tipoPedido(),
					precioPedido: parseFloat(self.importe().substring(0, self.importe().length - 2))
				};
				var data = {
					data: JSON.stringify(info),
					url: "pedido/guardarPedido",
					type: "post",
					contentType: 'application/json',
					success: function(response) {
						self.vaciarCarrito();
						self.gastosEnvio("");
						self.importe("");
					},
					error: function(response) {
						self.error(response.responseJSON.errorMessage);
					}
				};
				$.ajax(data);
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
