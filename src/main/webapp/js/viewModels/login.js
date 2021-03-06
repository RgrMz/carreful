define(['knockout', 'appController', 'ojs/ojmodule-element-utils', 'accUtils',
	'jquery', "ojs/ojbootstrap", "ojs/ojknockout", "ojs/ojinputtext", "ojs/ojformlayout", "ojs/ojlabel", "ojs/ojbutton"], function(ko, app, moduleUtils, accUtils, $) {

		class LoginViewModel {
			constructor() {

				// Recipe 

				this.formState = ko.observable("enabled");
				this.valueEnabled = ko.observable("yes");
				this.disableControls = ko.computed(() => {
					if (this.formState() === "disabled") {
						return true;
					}
					return false;
				});
				this.showValue = ko.computed(() => {
					if (this.valueEnabled() === "no") {
						return false;
					}
					return true;
				});
				this.readonlyControls = ko.computed(() => {
					if (this.formState() === "readonly") {
						return true;
					}
					return false;
				});
				this.hideButton = ko.computed(() => {
					if (this.formState() === "readonly") {
						return "oj-sm-hide";
					}
					return "";
				});
				this.labelEdge = ko.observable("inside");

				var self = this;

				self.email = ko.observable("");
				self.pwd = ko.observable("");
				self.message = ko.observable();
				self.error = ko.observable();
				self.recuerdame = ko.observable(true);

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

			login() {
				var self = this;
				var info = {
					email: this.email(),
					pwd: this.pwd(),
					recuerdame: this.recuerdame()
				};
				var data = {
					data: JSON.stringify(info),
					url: "user/login",
					type: "post",
					contentType: 'application/json',
					success: function() {
						app.router.go({ path: "menu" });
					},
					error: function(response) {
						document.getElementById("alerta-error").style.display = "block";
						document.getElementById("alerta-pwd").style.display = "none";
						document.getElementById("alerta-error").style.background = "rgb(255, 0, 0)";
						self.error(response.responseJSON.errorMessage);
					}
				};
				$.ajax(data);
			}

			recoverPwd() {
				var self = this;
				var data = {
					url: "user/recoverPwd?email=" + self.email(),
					type: "get",
					contentType: 'application/json',
					success: function() {
						document.getElementById("alerta-pwd").style.display = "block";
						document.getElementById("alerta-pwd").style.background = "rgb(75, 220, 65)";
						document.getElementById("alerta-error").style.display = "none";
						self.message("Si est??s dado de alta, te habr?? llegado un correo electr??nico");
					},
					error: function(response) {
						document.getElementById("alerta-error").style.display = "block";
						document.getElementById("alerta-error").style.background = "rgb(255, 0, 0)";
						document.getElementById("alerta-pwd").style.display = "none";
						self.error(response.responseJSON.errorMessage);
					}
				};
				$.ajax(data);
			}

			recordarUsuario() {
				var self = this;
				var data = {
					url: "user/getUsuarioRecordado",
					type: "get",
					contentType: 'application/json',
					success: function(response) {
						if (response) {
							self.email(response[0]);
							self.pwd(response[1]);
							self.recuerdame(true)
						}
					},
					error: function(response) {
						document.getElementById("alerta-pwd").style.display = "none";
						document.getElementById("alerta-error").style.display = "block";
						document.getElementById("alerta-error").style.background = "rgb(255, 0, 0)";
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
				this.recordarUsuario();
			}

			disconnected() {
				// Implement if needed
			}

			transitionCompleted() {
				// Implement if needed
			}
		}

		return LoginViewModel;
	});
