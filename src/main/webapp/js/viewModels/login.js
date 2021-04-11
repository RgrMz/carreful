define([ 'knockout', 'appController', 'ojs/ojmodule-element-utils', 'accUtils',
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
			
			// Header Config
			self.headerConfig = ko.observable({
				'view' : [],
				'viewModel' : null
			});
			moduleUtils.createView({
				'viewPath' : 'views/header.html'
			}).then(function(view) {
				self.headerConfig({
					'view' : view,
					'viewModel' : app.getHeaderModel()
				})
			})
		}

		login() {
			var self = this;
			var info = {
				email : this.email(),
				pwd : this.pwd()
			};
			var data = {
				data : JSON.stringify(info),
				url : "user/login",
				type : "post",
				contentType : 'application/json',
				success : function(response) {
					app.router.go( { path : "menu"} );
				},
				error : function(response) {
					self.error(response.responseJSON.errorMessage);
				}
			};
			$.ajax(data);
		}
		
		recoverPwd() {
			document.getElementById("alerta-pwd").style.display = "block";
			var self = this;
			var data = {
				url : "user/recoverPwd?email=" + self.email(),
				type : "get",
				contentType : 'application/json',
				success : function(response) {
					self.message("Si estás dado de alta, te habrá llegado un correo electrónico");
				},
				error : function(response) {
					self.error(response.responseJSON.errorMessage);
				}
			};
			$.ajax(data);
		}
		
		register() {
			app.router.go( { path : "register" } );
		}

		connected() {
			accUtils.announce('Login page loaded.');
			document.title = "Login";
		};

		disconnected() {
			// Implement if needed
		};

		transitionCompleted() {
			// Implement if needed
		};
	}

	return LoginViewModel;
});
