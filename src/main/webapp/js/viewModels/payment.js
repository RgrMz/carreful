define(['knockout', 'appController', 'ojs/ojmodule-element-utils', 'accUtils',
	'jquery', "ojs/ojbootstrap", "ojs/ojknockout", "ojs/ojinputtext", "ojs/ojformlayout", "ojs/ojlabel", "ojs/ojbutton"], function(ko, app, moduleUtils, accUtils, $) {

		class PaymentViewModel {
			constructor() {

				var self = this;
				// Para paginas que no son single page (index.html con un router que va cambiando la vista) seria asi
				self.stripe = Stripe('pk_test_51Idbt6HmArDnS3pXvdBN6zJ0jyaJS65zY1vMv4z0wfoG3vyjTEoPYMjpGU9G04ZLEUCokpTsvirO806CJ2xN8EwW00kXh8tv4f');

				self.message = ko.observable("");
				self.error = ko.observable("");

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
				var self = this;
				accUtils.announce('Login page loaded.');
				document.title = "Login";
				this.solicitarPreautorizacion();
			};

			solicitarPreautorizacion() {

				var self = this;

				// The items the customer wants to buy
				var purchase = {
					items: [{ id: "xl-tshirt" }]
				};
				// Disable the button until we have Stripe set up on the page
				document.querySelector("button").disabled = true;
				fetch("payments/solicitarPreautorizacion", {
					method: "POST",
					headers: {
						"Content-Type": "application/json"
					},
					body: JSON.stringify(purchase)
				})
					.then(function(result) {
						return result.json();
					})
					.then(function(data) {
						var elements = self.stripe.elements();
						let style = {
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

						let card = elements.create("card", { style: style });
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
							payWithCard(stripe, card, data.clientSecret);
						});
					});
				// Calls stripe.confirmCardPayment
				// If the card requires authentication Stripe shows a pop-up modal to
				// prompt the user to enter authentication details without leaving your page.
				var payWithCard = function(stripe, card, clientSecret) {
					loading(true);
					stripe
						.confirmCardPayment(clientSecret, {
							payment_method: {
								card: card
							}
						})
						.then(function(result) {
							if (result.error) {
								// Show error to your customer
								showError(result.error.message);
							} else {
								// The payment succeeded!
								orderComplete(result.paymentIntent.id);
							}
						});
				};

				/* ------- UI helpers ------- */

				// Shows a success message when the payment is complete
				var orderComplete = function(paymentIntentId) {
					loading(false);
					document
						.querySelector(".result-message a")
						.setAttribute(
							"href",
							"https://dashboard.stripe.com/test/payments/" + paymentIntentId
						);
					document.querySelector(".result-message").classList.remove("hidden");
					document.querySelector("button").disabled = true;
				};
				// Show the customer the error from Stripe if their card fails to charge
				var showError = function(errorMsgText) {
					loading(false);
					var errorMsg = document.querySelector("#card-error");
					errorMsg.textContent = errorMsgText;
					setTimeout(function() {
						errorMsg.textContent = "";
					}, 4000);
				};
				// Show a spinner on payment submission
				var loading = function(isLoading) {
					if (isLoading) {
						// Disable the button and show a spinner
						document.querySelector("button").disabled = true;
						document.querySelector("#spinner").classList.remove("hidden");
						document.querySelector("#button-text").classList.add("hidden");
					} else {
						document.querySelector("button").disabled = false;
						document.querySelector("#spinner").classList.add("hidden");
						document.querySelector("#button-text").classList.remove("hidden");
					}
				};
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
