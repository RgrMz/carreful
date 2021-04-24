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

				self.nombre = ko.observable();
				self.apellidos = ko.observable();
				self.email = ko.observable();
				self.telefonoMovil = ko.observable();
				self.direccion = ko.observable();
				self.ciudad = ko.observable();
				self.provincia = ko.observable();
				self.codigoPostal = ko.observable();
				self.pais = ko.observable();
				
				self.domicilio = ko.observable();
				self.domicilioExpress = ko.observable();
				self.recoger = ko.observable();

				self.gastosEnvio = ko.observable();

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

			connected() {
				accUtils.announce('Payment page loaded.');
				document.title = "Pago";
				this.solicitarPreautorizacion();
				this.getCarrito();
			};

			solicitarPreautorizacion() {

				var self = this;

				// The items the customer wants to buy
				let purchase = {
					items: [{ id: "xl-tshirt" }]
				};

				let data = {
					data: JSON.stringify(purchase),
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
					document.querySelector("button").disabled = event.empty;
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
						}
					}
				});
			}

			disconnected() {
				// Implement if needed
			};

			transitionCompleted() {
				// Implement if needed
			};
			
			
		}

		return PaymentViewModel;
	});
