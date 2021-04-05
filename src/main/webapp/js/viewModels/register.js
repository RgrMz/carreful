/**
 * @license
 * Copyright (c) 2014, 2020, Oracle and/or its affiliates.
 * Licensed under The Universal Permissive License (UPL), Version 1.0
 * as shown at https://oss.oracle.com/licenses/upl/
 * @ignore
 */
/*
 * Your dashboard ViewModel code goes here
 */
define(['knockout', 'appController', 'ojs/ojmodule-element-utils', 'accUtils',
	'jquery', "ojs/ojbootstrap", "ojs/ojknockout", "ojs/ojinputtext", "ojs/ojformlayout", "ojs/ojbutton", "require", "exports"],
	function(ko, app, moduleUtils, accUtils, $, Bootstrap, require, exports) {

		function RegisterViewModel() {
			var self = this;

			// Recipe for file picker

			this.fileNames = ko.observable();
			this.selectListener = (files) => {
				this.fileNames(Array.prototype.map.call(files, (file) => {
					return file.name;
				}));
			};
			this.selectFiles = (event) => {
				FilePickerUtils.pickFiles(this.selectListener, {
					accept: [],
					capture: "none",
					selectionMode: "single",
				});
			};

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

			self.userName = ko.observable("");
			self.email = ko.observable("");
			self.pwd1 = ko.observable("");
			self.pwd2 = ko.observable("");
			self.picture = ko.observable();

			self.message = ko.observable();
			self.error = ko.observable();

			self.setPicture = function(widget, event) {
				var file = event.target.files[0];
				var reader = new FileReader();
				reader.onload = function() {
					self.picture("data:image/png;base64," + btoa(reader.result));
				}
				reader.readAsBinaryString(file);
			}

			self.register = function() {
				var info = {
					userName: self.userName(),
					email: self.email(),
					pwd: self.pwd1(),
					pwd2: self.pwd2(),
					picture: self.picture()
				};
				var data = {
					data: JSON.stringify(info),
					url: "user/register",
					type: "put",
					contentType: 'application/json',
					success: function(response) {
						self.error("");
						self.message("Te hemos enviado un correo para confirmar tu registro");
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
				// Implement further logic if needed
			};

			self.disconnected = function() {
				// Implement if needed
			};

			self.transitionCompleted = function() {
				// Implement if needed
			};
		}

		return RegisterViewModel;
	});
