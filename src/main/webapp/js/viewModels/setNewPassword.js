define(['knockout', 'appController', 'ojs/ojmodule-element-utils', 'accUtils',
	'jquery'], function(ko, app, moduleUtils, accUtils, $) {

		function SetNewPasswordViewModel() {
			var self = this;

			self.email = ko.observable("");
			self.newPwd1 = ko.observable("");
			self.newPwd2 = ko.observable("");

			self.message = ko.observable();
			self.error = ko.observable();

			self.setNewPassword = function() {
				var info = {
					email: self.email(),
					newPwd1: self.newPwd1(),
					newPwd2: self.newPwd2()
				};
				var data = {
					data: JSON.stringify(info),
					url: "user/setNewPassword",
					type: "put",
					contentType: 'application/json',
					success: function() {
						self.error("");
						self.message("Se ha cambiado correctamente la contraseña.");
					},
					error: function(response) {
						self.message("");
						self.error(response.responseJSON.errorMessage);
					}
				};
				$.ajax(data);
			}

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

			self.connected = function() {
				accUtils.announce('Register page loaded.');
				document.title = "Registro";

				const queryString = window.location.search;
				const urlParams = new URLSearchParams(queryString);
				self.email(urlParams.get('email'));
			};

			self.disconnected = function() {
				// Implement if needed
			};

			self.transitionCompleted = function() {
				// Implement if needed
			};
		}

		return SetNewPasswordViewModel;
	});
