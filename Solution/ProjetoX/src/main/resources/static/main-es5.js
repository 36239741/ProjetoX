(window["webpackJsonp"] = window["webpackJsonp"] || []).push([["main"],{

/***/ "./$$_lazy_route_resource lazy recursive":
/*!******************************************************!*\
  !*** ./$$_lazy_route_resource lazy namespace object ***!
  \******************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

function webpackEmptyAsyncContext(req) {
	// Here Promise.resolve().then() is used instead of new Promise() to prevent
	// uncaught exception popping up in devtools
	return Promise.resolve().then(function() {
		var e = new Error("Cannot find module '" + req + "'");
		e.code = 'MODULE_NOT_FOUND';
		throw e;
	});
}
webpackEmptyAsyncContext.keys = function() { return []; };
webpackEmptyAsyncContext.resolve = webpackEmptyAsyncContext;
module.exports = webpackEmptyAsyncContext;
webpackEmptyAsyncContext.id = "./$$_lazy_route_resource lazy recursive";

/***/ }),

/***/ "./node_modules/raw-loader/index.js!./src/app/Modulos/login/login/login.component.html":
/*!************************************************************************************!*\
  !*** ./node_modules/raw-loader!./src/app/Modulos/login/login/login.component.html ***!
  \************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = "<div class=\"container-form\" fxLayout=\"column\" fxLayoutAlign=\"center center\">\n    <div class=\"form\" [class.mat-elevation-z8]=\"true\" fxLayout=\"column\" fxLayoutAlign=\"center center\" >\n      <img src=\"../../../../assets/img/logo.png\" class=\"logo\">\n      <td-dynamic-forms #form [elements]=\"elements\" (submit)=\"login()\" >\n        <ng-template let-element ngFor [ngForOf]=\"elements\">\n          <ng-template let-control=\"control\" [tdDynamicFormsError]=\"element.name\">\n            <span *ngIf=\"control.touched || !control.pristine\">\n              <app-errors\n              *ngIf=\"control.hasError('minlength')\"\n              text=\"Tamanho minimo: {{element.minLength}}\">\n            </app-errors>\n              <app-errors\n              *ngIf=\"control.hasError('maxlength')\"\n              text=\"Tamanho maximo: {{element.minLength}}\">\n            </app-errors>\n            </span>\n          </ng-template>\n        </ng-template>\n          <button mat-raised-button color=\"primary\" [disabled]=\"!this.form.valid\">Login</button>\n          <div id=\"errorLogin\" fxLayout='column' fxLayoutAlign='center center'>\n            <app-errors [text]='errorLogin'></app-errors>\n          </div>\n      </td-dynamic-forms>\n    </div>\n</div>\n\n\n"

/***/ }),

/***/ "./node_modules/raw-loader/index.js!./src/app/app.component.html":
/*!**************************************************************!*\
  !*** ./node_modules/raw-loader!./src/app/app.component.html ***!
  \**************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = "<router-outlet></router-outlet>\n\n\n"

/***/ }),

/***/ "./node_modules/raw-loader/index.js!./src/app/shared/components/clock-biometria/clock-biometria.component.html":
/*!************************************************************************************************************!*\
  !*** ./node_modules/raw-loader!./src/app/shared/components/clock-biometria/clock-biometria.component.html ***!
  \************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = "<div fxLayout=\"row\" fxLayoutGap=\"20px\" fxLayoutAlign=\"center center\">\n        <span>Tempo de captura da biometria: {{ timerClock }}</span>\n        <button mat-flat-button color=\"primary\" (click)=\"cancelCaptureFingerPrint()\" matTooltip=\"Cancela a captura da biometria\" >Cancelar</button>\n</div>\n"

/***/ }),

/***/ "./node_modules/raw-loader/index.js!./src/app/shared/components/errors/errors.component.html":
/*!******************************************************************************************!*\
  !*** ./node_modules/raw-loader!./src/app/shared/components/errors/errors.component.html ***!
  \******************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = "<span>{{ text }}</span>\n"

/***/ }),

/***/ "./src/app/Modulos/login/login.module.ts":
/*!***********************************************!*\
  !*** ./src/app/Modulos/login/login.module.ts ***!
  \***********************************************/
/*! exports provided: LoginModule */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "LoginModule", function() { return LoginModule; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/common */ "./node_modules/@angular/common/fesm5/common.js");
/* harmony import */ var _login_login_component__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./login/login.component */ "./src/app/Modulos/login/login/login.component.ts");
/* harmony import */ var _angular_common_http__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! @angular/common/http */ "./node_modules/@angular/common/fesm5/http.js");
/* harmony import */ var _angular_forms__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! @angular/forms */ "./node_modules/@angular/forms/fesm5/forms.js");
/* harmony import */ var _shared_components_errors_errors_module__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! ../../shared/components/errors/errors.module */ "./src/app/shared/components/errors/errors.module.ts");
/* harmony import */ var src_app_core_material_material_module__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! src/app/core/material/material.module */ "./src/app/core/material/material.module.ts");








var LoginModule = /** @class */ (function () {
    function LoginModule() {
    }
    LoginModule = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["NgModule"])({
            declarations: [_login_login_component__WEBPACK_IMPORTED_MODULE_3__["LoginComponent"]],
            imports: [
                _angular_common__WEBPACK_IMPORTED_MODULE_2__["CommonModule"],
                _angular_common_http__WEBPACK_IMPORTED_MODULE_4__["HttpClientModule"],
                _angular_forms__WEBPACK_IMPORTED_MODULE_5__["ReactiveFormsModule"],
                _angular_forms__WEBPACK_IMPORTED_MODULE_5__["FormsModule"],
                _shared_components_errors_errors_module__WEBPACK_IMPORTED_MODULE_6__["ErrorsModule"],
                src_app_core_material_material_module__WEBPACK_IMPORTED_MODULE_7__["MaterialModule"]
            ]
        })
    ], LoginModule);
    return LoginModule;
}());



/***/ }),

/***/ "./src/app/Modulos/login/login/login.component.css":
/*!*********************************************************!*\
  !*** ./src/app/Modulos/login/login/login.component.css ***!
  \*********************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = ".container-form{\n  width: 100%;\n  height: 100%;\n}\n.form{\n  width: 600px;\n  height: 500px;\n  background-color: #e9e9e9;\n  border-radius: 10px;\n}\n#errorLogin{\n  padding: 30px;\n}\n.logo {\n    width: 60%;\n    margin-bottom: 20px;\n}\n\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbInNyYy9hcHAvTW9kdWxvcy9sb2dpbi9sb2dpbi9sb2dpbi5jb21wb25lbnQuY3NzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiJBQUFBO0VBQ0UsV0FBVztFQUNYLFlBQVk7QUFDZDtBQUNBO0VBQ0UsWUFBWTtFQUNaLGFBQWE7RUFDYix5QkFBeUI7RUFDekIsbUJBQW1CO0FBQ3JCO0FBRUE7RUFDRSxhQUFhO0FBQ2Y7QUFDQTtJQUNJLFVBQVU7SUFDVixtQkFBbUI7QUFDdkIiLCJmaWxlIjoic3JjL2FwcC9Nb2R1bG9zL2xvZ2luL2xvZ2luL2xvZ2luLmNvbXBvbmVudC5jc3MiLCJzb3VyY2VzQ29udGVudCI6WyIuY29udGFpbmVyLWZvcm17XG4gIHdpZHRoOiAxMDAlO1xuICBoZWlnaHQ6IDEwMCU7XG59XG4uZm9ybXtcbiAgd2lkdGg6IDYwMHB4O1xuICBoZWlnaHQ6IDUwMHB4O1xuICBiYWNrZ3JvdW5kLWNvbG9yOiAjZTllOWU5O1xuICBib3JkZXItcmFkaXVzOiAxMHB4O1xufVxuXG4jZXJyb3JMb2dpbntcbiAgcGFkZGluZzogMzBweDtcbn1cbi5sb2dvIHtcbiAgICB3aWR0aDogNjAlO1xuICAgIG1hcmdpbi1ib3R0b206IDIwcHg7XG59XG4iXX0= */"

/***/ }),

/***/ "./src/app/Modulos/login/login/login.component.ts":
/*!********************************************************!*\
  !*** ./src/app/Modulos/login/login/login.component.ts ***!
  \********************************************************/
/*! exports provided: LoginComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "LoginComponent", function() { return LoginComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _covalent_dynamic_forms__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @covalent/dynamic-forms */ "./node_modules/@covalent/dynamic-forms/fesm5/covalent-dynamic-forms.js");
/* harmony import */ var src_app_shared_Services_login_service__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! src/app/shared/Services/login.service */ "./src/app/shared/Services/login.service.ts");
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! @angular/router */ "./node_modules/@angular/router/fesm5/router.js");





var LoginComponent = /** @class */ (function () {
    function LoginComponent(autenticacaoLogin, route) {
        this.autenticacaoLogin = autenticacaoLogin;
        this.route = route;
        this.errorLogin = '';
        this.elements = [
            {
                name: 'email',
                label: 'Username',
                type: _covalent_dynamic_forms__WEBPACK_IMPORTED_MODULE_2__["TdDynamicElement"].Input,
                minLength: 4,
                maxLength: 50,
                required: true
            },
            {
                name: 'senha',
                label: 'Password',
                type: _covalent_dynamic_forms__WEBPACK_IMPORTED_MODULE_2__["TdDynamicElement"].Password,
                minLength: 4,
                maxLength: 12,
                required: true
            }
        ];
    }
    LoginComponent.prototype.ngOnInit = function () {
    };
    /*  Funcao ira fazer uma requisao post para o backend enviando email e senha e recebera o token de autenticacao
      em caso de erro ira imprimir na tela o erro
      @return void */
    LoginComponent.prototype.login = function () {
        var _this = this;
        var API_URL = 'http://localhost:4200';
        var email = this.form.form.get('email').value;
        var senha = this.form.form.get('senha').value;
        if (this.form.valid) {
            this.subscription = this.autenticacaoLogin.autenticacaoLogin(email, senha).subscribe(function () {
                _this.route.navigate(['/home']);
            }, function (err) {
                console.log(err);
                _this.form.refresh();
                if (err.status === 0) {
                    _this.errorLogin = 'Email ou senha incorretos.';
                }
            });
        }
    };
    LoginComponent.prototype.ngOnDestroy = function () {
        this.subscription.unsubscribe();
    };
    LoginComponent.ctorParameters = function () { return [
        { type: src_app_shared_Services_login_service__WEBPACK_IMPORTED_MODULE_3__["LoginService"] },
        { type: _angular_router__WEBPACK_IMPORTED_MODULE_4__["Router"] }
    ]; };
    tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["ViewChild"])('form', { static: false })
    ], LoginComponent.prototype, "form", void 0);
    LoginComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
            selector: 'app-login',
            template: __webpack_require__(/*! raw-loader!./login.component.html */ "./node_modules/raw-loader/index.js!./src/app/Modulos/login/login/login.component.html"),
            styles: [__webpack_require__(/*! ./login.component.css */ "./src/app/Modulos/login/login/login.component.css")]
        })
    ], LoginComponent);
    return LoginComponent;
}());



/***/ }),

/***/ "./src/app/app-routing.module.ts":
/*!***************************************!*\
  !*** ./src/app/app-routing.module.ts ***!
  \***************************************/
/*! exports provided: AppRoutingModule */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AppRoutingModule", function() { return AppRoutingModule; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/router */ "./node_modules/@angular/router/fesm5/router.js");
/* harmony import */ var _Modulos_login_login_login_component__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./Modulos/login/login/login.component */ "./src/app/Modulos/login/login/login.component.ts");




var routes = [
    { path: '', component: _Modulos_login_login_login_component__WEBPACK_IMPORTED_MODULE_3__["LoginComponent"] },
    { path: 'home', loadChildren: function () { return __webpack_require__.e(/*! import() | Modulos-Layout-layout-module */ "default~Modulos-Layout-layout-module~app-Modulos-contratos-contratos-module").then(__webpack_require__.bind(null, /*! ./Modulos/Layout/layout.module */ "./src/app/Modulos/Layout/layout.module.ts")).then(function (m) { return m.LayoutModule; }); } },
    { path: 'contratos', loadChildren: function () { return Promise.all(/*! import() | app-Modulos-contratos-contratos-module */[__webpack_require__.e("default~Modulos-Layout-layout-module~app-Modulos-contratos-contratos-module"), __webpack_require__.e("app-Modulos-contratos-contratos-module")]).then(__webpack_require__.bind(null, /*! ../app/Modulos/contratos/contratos.module */ "./src/app/Modulos/contratos/contratos.module.ts")).then(function (m) { return m.ContratosModule; }); } },
];
var AppRoutingModule = /** @class */ (function () {
    function AppRoutingModule() {
    }
    AppRoutingModule = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["NgModule"])({
            imports: [_angular_router__WEBPACK_IMPORTED_MODULE_2__["RouterModule"].forRoot(routes)],
            exports: [_angular_router__WEBPACK_IMPORTED_MODULE_2__["RouterModule"]]
        })
    ], AppRoutingModule);
    return AppRoutingModule;
}());



/***/ }),

/***/ "./src/app/app.component.css":
/*!***********************************!*\
  !*** ./src/app/app.component.css ***!
  \***********************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = "\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IiIsImZpbGUiOiJzcmMvYXBwL2FwcC5jb21wb25lbnQuY3NzIn0= */"

/***/ }),

/***/ "./src/app/app.component.ts":
/*!**********************************!*\
  !*** ./src/app/app.component.ts ***!
  \**********************************/
/*! exports provided: AppComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AppComponent", function() { return AppComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/router */ "./node_modules/@angular/router/fesm5/router.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var hammerjs__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! hammerjs */ "./node_modules/hammerjs/hammer.js");
/* harmony import */ var hammerjs__WEBPACK_IMPORTED_MODULE_3___default = /*#__PURE__*/__webpack_require__.n(hammerjs__WEBPACK_IMPORTED_MODULE_3__);
/* harmony import */ var _covalent_core_loading__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! @covalent/core/loading */ "./node_modules/@covalent/core/fesm5/covalent-core-loading.js");
/* harmony import */ var rxjs_operators__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! rxjs/operators */ "./node_modules/rxjs/_esm5/operators/index.js");






var AppComponent = /** @class */ (function () {
    function AppComponent(loading, router) {
        this.loading = loading;
        this.router = router;
        this.loading.create({
            name: 'loading',
            type: _covalent_core_loading__WEBPACK_IMPORTED_MODULE_4__["LoadingType"].Circular,
            mode: _covalent_core_loading__WEBPACK_IMPORTED_MODULE_4__["LoadingMode"].Indeterminate,
            color: 'primary'
        });
    }
    AppComponent.prototype.intercept = function (req, next) {
        var xhr = req.clone({
            headers: req.headers.set('X-Requested-With', 'XMLHttpRequest')
        });
        return next.handle(xhr);
    };
    AppComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.router.events
            .pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_5__["filter"])(function (event) {
            return event instanceof _angular_router__WEBPACK_IMPORTED_MODULE_1__["NavigationStart"] ||
                event instanceof _angular_router__WEBPACK_IMPORTED_MODULE_1__["NavigationEnd"] ||
                event instanceof _angular_router__WEBPACK_IMPORTED_MODULE_1__["NavigationCancel"] ||
                event instanceof _angular_router__WEBPACK_IMPORTED_MODULE_1__["NavigationError"];
        })).subscribe(function (event) {
            if (event instanceof _angular_router__WEBPACK_IMPORTED_MODULE_1__["NavigationStart"]) {
                _this.registerLoading();
                return;
            }
            else {
                _this.resolverLoading();
            }
        });
    };
    AppComponent.prototype.registerLoading = function () {
        this.loading.register('loading');
    };
    AppComponent.prototype.resolverLoading = function () {
        this.loading.resolve('loading');
    };
    AppComponent.ctorParameters = function () { return [
        { type: _covalent_core_loading__WEBPACK_IMPORTED_MODULE_4__["TdLoadingService"] },
        { type: _angular_router__WEBPACK_IMPORTED_MODULE_1__["Router"] }
    ]; };
    AppComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_2__["Component"])({
            selector: 'app-root',
            template: __webpack_require__(/*! raw-loader!./app.component.html */ "./node_modules/raw-loader/index.js!./src/app/app.component.html"),
            styles: [__webpack_require__(/*! ./app.component.css */ "./src/app/app.component.css")]
        })
    ], AppComponent);
    return AppComponent;
}());



/***/ }),

/***/ "./src/app/app.module.ts":
/*!*******************************!*\
  !*** ./src/app/app.module.ts ***!
  \*******************************/
/*! exports provided: AppModule */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AppModule", function() { return AppModule; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var src_app_shared_components_clock_biometria_clock_biometria_module__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! src/app/shared/components/clock-biometria/clock-biometria.module */ "./src/app/shared/components/clock-biometria/clock-biometria.module.ts");
/* harmony import */ var _angular_platform_browser__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/platform-browser */ "./node_modules/@angular/platform-browser/fesm5/platform-browser.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_platform_browser_animations__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! @angular/platform-browser/animations */ "./node_modules/@angular/platform-browser/fesm5/animations.js");
/* harmony import */ var _app_routing_module__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ./app-routing.module */ "./src/app/app-routing.module.ts");
/* harmony import */ var _app_component__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! ./app.component */ "./src/app/app.component.ts");
/* harmony import */ var _Modulos_login_login_module__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! ./Modulos/login/login.module */ "./src/app/Modulos/login/login.module.ts");
/* harmony import */ var _angular_common_locales_pt__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! @angular/common/locales/pt */ "./node_modules/@angular/common/locales/pt.js");
/* harmony import */ var _angular_common_locales_pt__WEBPACK_IMPORTED_MODULE_8___default = /*#__PURE__*/__webpack_require__.n(_angular_common_locales_pt__WEBPACK_IMPORTED_MODULE_8__);
/* harmony import */ var _angular_common_http__WEBPACK_IMPORTED_MODULE_9__ = __webpack_require__(/*! @angular/common/http */ "./node_modules/@angular/common/fesm5/http.js");
/* harmony import */ var _core_interceptor_basic_interceptor_service__WEBPACK_IMPORTED_MODULE_10__ = __webpack_require__(/*! ./core/interceptor/basic-interceptor.service */ "./src/app/core/interceptor/basic-interceptor.service.ts");
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_11__ = __webpack_require__(/*! @angular/common */ "./node_modules/@angular/common/fesm5/common.js");
/* harmony import */ var _core_material_material_module__WEBPACK_IMPORTED_MODULE_12__ = __webpack_require__(/*! ./core/material/material.module */ "./src/app/core/material/material.module.ts");
/* harmony import */ var _covalent_core_loading__WEBPACK_IMPORTED_MODULE_13__ = __webpack_require__(/*! @covalent/core/loading */ "./node_modules/@covalent/core/fesm5/covalent-core-loading.js");
/* harmony import */ var _angular_material_dialog__WEBPACK_IMPORTED_MODULE_14__ = __webpack_require__(/*! @angular/material/dialog */ "./node_modules/@angular/material/esm5/dialog.es5.js");















Object(_angular_common__WEBPACK_IMPORTED_MODULE_11__["registerLocaleData"])(_angular_common_locales_pt__WEBPACK_IMPORTED_MODULE_8___default.a, 'pt');
var AppModule = /** @class */ (function () {
    function AppModule() {
    }
    AppModule = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_3__["NgModule"])({
            declarations: [
                _app_component__WEBPACK_IMPORTED_MODULE_6__["AppComponent"],
            ],
            imports: [
                _angular_platform_browser__WEBPACK_IMPORTED_MODULE_2__["BrowserModule"],
                _app_routing_module__WEBPACK_IMPORTED_MODULE_5__["AppRoutingModule"],
                _angular_platform_browser_animations__WEBPACK_IMPORTED_MODULE_4__["BrowserAnimationsModule"],
                _Modulos_login_login_module__WEBPACK_IMPORTED_MODULE_7__["LoginModule"],
                _angular_common_http__WEBPACK_IMPORTED_MODULE_9__["HttpClientModule"],
                _core_material_material_module__WEBPACK_IMPORTED_MODULE_12__["MaterialModule"],
                _covalent_core_loading__WEBPACK_IMPORTED_MODULE_13__["CovalentLoadingModule"],
                src_app_shared_components_clock_biometria_clock_biometria_module__WEBPACK_IMPORTED_MODULE_1__["ClockBiometriaModule"],
                _angular_material_dialog__WEBPACK_IMPORTED_MODULE_14__["MatDialogModule"]
            ],
            providers: [{
                    provide: _angular_common_http__WEBPACK_IMPORTED_MODULE_9__["HTTP_INTERCEPTORS"],
                    useClass: _core_interceptor_basic_interceptor_service__WEBPACK_IMPORTED_MODULE_10__["BasicInterceptorService"],
                    multi: true
                },
                { provide: _angular_core__WEBPACK_IMPORTED_MODULE_3__["LOCALE_ID"], useValue: 'pt' }],
            bootstrap: [_app_component__WEBPACK_IMPORTED_MODULE_6__["AppComponent"]]
        })
    ], AppModule);
    return AppModule;
}());



/***/ }),

/***/ "./src/app/core/interceptor/basic-interceptor.service.ts":
/*!***************************************************************!*\
  !*** ./src/app/core/interceptor/basic-interceptor.service.ts ***!
  \***************************************************************/
/*! exports provided: BasicInterceptorService */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "BasicInterceptorService", function() { return BasicInterceptorService; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");


var BasicInterceptorService = /** @class */ (function () {
    function BasicInterceptorService() {
    }
    BasicInterceptorService.prototype.intercept = function (req, next) {
        if (sessionStorage.getItem('currentUser') != null) {
            var basicHeader = req.clone({
                headers: req.headers.set('Authorization', 'Basic ' + sessionStorage.getItem('currentUser'))
            });
            return next.handle(basicHeader);
        }
        return next.handle(req.clone());
    };
    BasicInterceptorService = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Injectable"])()
    ], BasicInterceptorService);
    return BasicInterceptorService;
}());



/***/ }),

/***/ "./src/app/core/material/material.module.ts":
/*!**************************************************!*\
  !*** ./src/app/core/material/material.module.ts ***!
  \**************************************************/
/*! exports provided: MaterialModule */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "MaterialModule", function() { return MaterialModule; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/common */ "./node_modules/@angular/common/fesm5/common.js");
/* harmony import */ var _angular_flex_layout__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! @angular/flex-layout */ "./node_modules/@angular/flex-layout/esm5/flex-layout.es5.js");
/* harmony import */ var _covalent_dynamic_forms__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! @covalent/dynamic-forms */ "./node_modules/@covalent/dynamic-forms/fesm5/covalent-dynamic-forms.js");
/* harmony import */ var _covalent_core__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! @covalent/core */ "./node_modules/@covalent/core/fesm5/covalent-core.js");
/* harmony import */ var _angular_material__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! @angular/material */ "./node_modules/@angular/material/esm5/material.es5.js");







var MaterialModule = /** @class */ (function () {
    function MaterialModule() {
    }
    MaterialModule = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["NgModule"])({
            imports: [
                /* Angular Material */
                _angular_common__WEBPACK_IMPORTED_MODULE_2__["CommonModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatSelectModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatButtonModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatIconModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatInputModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatFormFieldModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatCardModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatTooltipModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatSelectModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatListModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatButtonModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatSnackBarModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatToolbarModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatSidenavModule"],
                /*Teradata Covalent*/
                _covalent_core__WEBPACK_IMPORTED_MODULE_5__["CovalentBreadcrumbsModule"],
                _covalent_core__WEBPACK_IMPORTED_MODULE_5__["CovalentDataTableModule"],
                _covalent_core__WEBPACK_IMPORTED_MODULE_5__["CovalentSearchModule"],
                _covalent_core__WEBPACK_IMPORTED_MODULE_5__["CovalentDialogsModule"],
                _covalent_core__WEBPACK_IMPORTED_MODULE_5__["CovalentFileModule"],
                _covalent_core__WEBPACK_IMPORTED_MODULE_5__["CovalentPagingModule"],
                _covalent_core__WEBPACK_IMPORTED_MODULE_5__["CovalentLoadingModule"],
                _covalent_core__WEBPACK_IMPORTED_MODULE_5__["CovalentMessageModule"],
                _covalent_dynamic_forms__WEBPACK_IMPORTED_MODULE_4__["CovalentDynamicFormsModule"],
                _covalent_core__WEBPACK_IMPORTED_MODULE_5__["CovalentLayoutModule"],
                /*Flex Layout Module*/
                _angular_flex_layout__WEBPACK_IMPORTED_MODULE_3__["FlexLayoutModule"]
            ],
            exports: [
                /* Angular Material */
                _angular_common__WEBPACK_IMPORTED_MODULE_2__["CommonModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatSelectModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatButtonModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatIconModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatInputModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatFormFieldModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatCardModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatTooltipModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatSelectModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatButtonModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatSnackBarModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatRadioModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatListModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatToolbarModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatProgressSpinnerModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_6__["MatSidenavModule"],
                /*Teradata Covalent*/
                _covalent_core__WEBPACK_IMPORTED_MODULE_5__["CovalentBreadcrumbsModule"],
                _covalent_core__WEBPACK_IMPORTED_MODULE_5__["CovalentDataTableModule"],
                _covalent_core__WEBPACK_IMPORTED_MODULE_5__["CovalentSearchModule"],
                _covalent_core__WEBPACK_IMPORTED_MODULE_5__["CovalentDialogsModule"],
                _covalent_core__WEBPACK_IMPORTED_MODULE_5__["CovalentFileModule"],
                _covalent_core__WEBPACK_IMPORTED_MODULE_5__["CovalentPagingModule"],
                _covalent_core__WEBPACK_IMPORTED_MODULE_5__["CovalentMessageModule"],
                _covalent_core__WEBPACK_IMPORTED_MODULE_5__["CovalentLoadingModule"],
                _covalent_dynamic_forms__WEBPACK_IMPORTED_MODULE_4__["CovalentDynamicFormsModule"],
                _covalent_core__WEBPACK_IMPORTED_MODULE_5__["CovalentLayoutModule"],
                /*Flex Layout Module*/
                _angular_flex_layout__WEBPACK_IMPORTED_MODULE_3__["FlexLayoutModule"]
            ],
            declarations: []
        })
    ], MaterialModule);
    return MaterialModule;
}());



/***/ }),

/***/ "./src/app/shared/Services/contrato.service.ts":
/*!*****************************************************!*\
  !*** ./src/app/shared/Services/contrato.service.ts ***!
  \*****************************************************/
/*! exports provided: ContratoService */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "ContratoService", function() { return ContratoService; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_common_http__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/common/http */ "./node_modules/@angular/common/fesm5/http.js");
/* harmony import */ var rxjs_operators__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! rxjs/operators */ "./node_modules/rxjs/_esm5/operators/index.js");
/* harmony import */ var rxjs__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! rxjs */ "./node_modules/rxjs/_esm5/index.js");





var API_URL = 'http://localhost:8080/v1';
var ContratoService = /** @class */ (function () {
    function ContratoService(httpClient) {
        this.httpClient = httpClient;
    }
    // Handle API errors
    ContratoService.prototype.handleError = function (error) {
        if (error.error instanceof ErrorEvent) {
            // A client-side or network error occurred. Handle it accordingly.
            console.error('An error occurred:', error.error.message);
        }
        else {
            // The backend returned an unsuccessful response code.
            // The response body may contain clues as to what went wrong,
            console.error("Backend returned code " + error.status + ", " +
                ("body was: " + error.error));
        }
        // return an observable with a user-facing error message
        return Object(rxjs__WEBPACK_IMPORTED_MODULE_4__["throwError"])('Something bad happened; please try again later.');
    };
    ;
    ContratoService.prototype.findAllContratos = function (page, size, sort, atributo) {
        return this.httpClient.get(API_URL + '/contratos?page=' + page + "&size=" + size + "&sort=" + sort + "&atributo=" + atributo).pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_3__["map"])(function (data) { return data; }), Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_3__["catchError"])(this.handleError));
    };
    ContratoService.prototype.findByContrato = function (numero) {
        return this.httpClient.get(API_URL + '/contratos/' + numero).pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_3__["map"])(function (data) { return data; }), Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_3__["catchError"])(this.handleError));
    };
    ContratoService.prototype.findByFilters = function (nome, numero, page, size, statusContrato, sort, atributo) {
        return this.httpClient.get(API_URL + '/contratos/filter?nomePaciente=' + nome +
            '&numero=' + numero + '&ativo=' + statusContrato + '&page=' + page + '&size=' + size + "&sort=" + sort + "&atributo=" + atributo).pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_3__["map"])(function (data) { return data; }), Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_3__["catchError"])(this.handleError));
    };
    ContratoService.prototype.findByBiometria = function () {
        return this.httpClient.get(API_URL + '/contratos/find-by-biometria');
    };
    ContratoService.prototype.gerarDesconto = function (numeroContrato, valorDesconto) {
        return this.httpClient.post(API_URL + '/contratos/desconto?numeroContrato=' + numeroContrato, valorDesconto);
    };
    ContratoService.prototype.saveBiometria = function (numeroContrato) {
        return this.httpClient.post(API_URL + '/contratos/save-biometria', numeroContrato);
    };
    ContratoService.prototype.cancelCapture = function () {
        return this.httpClient.get(API_URL + '/contratos/cancel-capture');
    };
    ContratoService.prototype.importContratos = function (xlsx) {
        return this.httpClient.post(API_URL + '/contratos', xlsx);
    };
    ContratoService.ctorParameters = function () { return [
        { type: _angular_common_http__WEBPACK_IMPORTED_MODULE_2__["HttpClient"] }
    ]; };
    ContratoService = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Injectable"])({
            providedIn: 'root'
        })
    ], ContratoService);
    return ContratoService;
}());



/***/ }),

/***/ "./src/app/shared/Services/login.service.ts":
/*!**************************************************!*\
  !*** ./src/app/shared/Services/login.service.ts ***!
  \**************************************************/
/*! exports provided: LoginService */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "LoginService", function() { return LoginService; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_common_http__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/common/http */ "./node_modules/@angular/common/fesm5/http.js");
/* harmony import */ var rxjs_operators__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! rxjs/operators */ "./node_modules/rxjs/_esm5/operators/index.js");




var API_URL = 'http://localhost:8080/v1';
var LoginService = /** @class */ (function () {
    function LoginService(http) {
        this.http = http;
    }
    LoginService.prototype.autenticacaoLogin = function (email, senha) {
        var basicEncoder = btoa(email + ':' + senha);
        var headers = new _angular_common_http__WEBPACK_IMPORTED_MODULE_2__["HttpHeaders"]({ Authorization: 'Basic ' + basicEncoder });
        return this.http.get(API_URL + '/login', { headers: headers }).pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_3__["map"])(function (userData) {
            sessionStorage.setItem('username', email);
            sessionStorage.setItem('currentUser', basicEncoder);
            return userData;
        }));
    };
    LoginService.prototype.isUserLoggedIn = function () {
        var user = sessionStorage.getItem('username');
        return !(user === null);
    };
    LoginService.ctorParameters = function () { return [
        { type: _angular_common_http__WEBPACK_IMPORTED_MODULE_2__["HttpClient"] }
    ]; };
    LoginService = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Injectable"])({
            providedIn: 'root'
        })
    ], LoginService);
    return LoginService;
}());



/***/ }),

/***/ "./src/app/shared/Services/toast.service.ts":
/*!**************************************************!*\
  !*** ./src/app/shared/Services/toast.service.ts ***!
  \**************************************************/
/*! exports provided: ToastService */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "ToastService", function() { return ToastService; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_material__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/material */ "./node_modules/@angular/material/esm5/material.es5.js");



var ToastService = /** @class */ (function () {
    function ToastService(snackBarService) {
        this.snackBarService = snackBarService;
    }
    /**
     * Retorna uma mensagem de sucesso para o usuário.
     * @param message
     * @param duration
     */
    ToastService.prototype.toastSuccess = function (message, duration) {
        if (duration === void 0) { duration = 5000; }
        var config = new _angular_material__WEBPACK_IMPORTED_MODULE_2__["MatSnackBarConfig"]();
        config.duration = duration;
        config.panelClass = ['toast-success', 'toast'];
        this.snackBarService.open(message, '', config);
    };
    /**
     * Retorna uma mensagem de erro para o usuário.
     * @param message
     * @param duration
     */
    ToastService.prototype.toastError = function (message, duration) {
        if (duration === void 0) { duration = 5000; }
        var config = new _angular_material__WEBPACK_IMPORTED_MODULE_2__["MatSnackBarConfig"]();
        config.duration = duration;
        config.panelClass = ['toast-error', 'toast'];
        this.snackBarService.open(message, '', config);
    };
    /**
     * Retorna uma mensagem de alerta para o usuário.
     * @param message
     * @param duration
     */
    ToastService.prototype.toastWarnning = function (message, duration) {
        if (duration === void 0) { duration = 5000; }
        var config = new _angular_material__WEBPACK_IMPORTED_MODULE_2__["MatSnackBarConfig"]();
        config.duration = duration;
        config.panelClass = ['toast-warnning', 'toast'];
        this.snackBarService.open(message, '', config);
    };
    ToastService.ctorParameters = function () { return [
        { type: _angular_material__WEBPACK_IMPORTED_MODULE_2__["MatSnackBar"] }
    ]; };
    ToastService = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Injectable"])({
            providedIn: 'root'
        })
    ], ToastService);
    return ToastService;
}());



/***/ }),

/***/ "./src/app/shared/components/clock-biometria/clock-biometria.component.css":
/*!*********************************************************************************!*\
  !*** ./src/app/shared/components/clock-biometria/clock-biometria.component.css ***!
  \*********************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = "\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IiIsImZpbGUiOiJzcmMvYXBwL3NoYXJlZC9jb21wb25lbnRzL2Nsb2NrLWJpb21ldHJpYS9jbG9jay1iaW9tZXRyaWEuY29tcG9uZW50LmNzcyJ9 */"

/***/ }),

/***/ "./src/app/shared/components/clock-biometria/clock-biometria.component.ts":
/*!********************************************************************************!*\
  !*** ./src/app/shared/components/clock-biometria/clock-biometria.component.ts ***!
  \********************************************************************************/
/*! exports provided: ClockBiometriaComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "ClockBiometriaComponent", function() { return ClockBiometriaComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Services_toast_service__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../../Services/toast.service */ "./src/app/shared/Services/toast.service.ts");
/* harmony import */ var _Services_contrato_service__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../../Services/contrato.service */ "./src/app/shared/Services/contrato.service.ts");
/* harmony import */ var rxjs__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! rxjs */ "./node_modules/rxjs/_esm5/index.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_material__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! @angular/material */ "./node_modules/@angular/material/esm5/material.es5.js");
/* harmony import */ var rxjs_operators__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! rxjs/operators */ "./node_modules/rxjs/_esm5/operators/index.js");







var ClockBiometriaComponent = /** @class */ (function () {
    function ClockBiometriaComponent(contratoService, tostService, snackBar) {
        this.contratoService = contratoService;
        this.tostService = tostService;
        this.snackBar = snackBar;
        this.tempoDeCapturaBiometria = 30;
    }
    ClockBiometriaComponent.prototype.ngOnInit = function () {
        this.timer();
    };
    ClockBiometriaComponent.prototype.timer = function () {
        var _this = this;
        var number = Object(rxjs__WEBPACK_IMPORTED_MODULE_3__["interval"])(1000);
        var takeForNumbers = number.pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_6__["take"])(this.tempoDeCapturaBiometria));
        this.subscription = takeForNumbers.subscribe(function (timer) { _this.timerClock = timer; });
    };
    ClockBiometriaComponent.prototype.cancelCaptureFingerPrint = function () {
        var _this = this;
        this.contratoService.cancelCapture().subscribe(function () {
            return function (error) {
                _this.snackBar.dismiss();
                _this.tostService.toastWarnning(error.error.message);
            };
        });
    };
    ClockBiometriaComponent.prototype.ngOnDestroy = function () {
        this.subscription.unsubscribe();
    };
    ClockBiometriaComponent.ctorParameters = function () { return [
        { type: _Services_contrato_service__WEBPACK_IMPORTED_MODULE_2__["ContratoService"] },
        { type: _Services_toast_service__WEBPACK_IMPORTED_MODULE_1__["ToastService"] },
        { type: _angular_material__WEBPACK_IMPORTED_MODULE_5__["MatSnackBar"] }
    ]; };
    ClockBiometriaComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_4__["Component"])({
            selector: 'app-clock-biometria',
            template: __webpack_require__(/*! raw-loader!./clock-biometria.component.html */ "./node_modules/raw-loader/index.js!./src/app/shared/components/clock-biometria/clock-biometria.component.html"),
            styles: [__webpack_require__(/*! ./clock-biometria.component.css */ "./src/app/shared/components/clock-biometria/clock-biometria.component.css")]
        })
    ], ClockBiometriaComponent);
    return ClockBiometriaComponent;
}());



/***/ }),

/***/ "./src/app/shared/components/clock-biometria/clock-biometria.module.ts":
/*!*****************************************************************************!*\
  !*** ./src/app/shared/components/clock-biometria/clock-biometria.module.ts ***!
  \*****************************************************************************/
/*! exports provided: ClockBiometriaModule */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "ClockBiometriaModule", function() { return ClockBiometriaModule; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_flex_layout__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/flex-layout */ "./node_modules/@angular/flex-layout/esm5/flex-layout.es5.js");
/* harmony import */ var _clock_biometria_component__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./clock-biometria.component */ "./src/app/shared/components/clock-biometria/clock-biometria.component.ts");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! @angular/common */ "./node_modules/@angular/common/fesm5/common.js");





var ClockBiometriaModule = /** @class */ (function () {
    function ClockBiometriaModule() {
    }
    ClockBiometriaModule = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_3__["NgModule"])({
            declarations: [_clock_biometria_component__WEBPACK_IMPORTED_MODULE_2__["ClockBiometriaComponent"]],
            imports: [
                _angular_common__WEBPACK_IMPORTED_MODULE_4__["CommonModule"],
                _angular_flex_layout__WEBPACK_IMPORTED_MODULE_1__["FlexLayoutModule"],
            ],
            entryComponents: [
                _clock_biometria_component__WEBPACK_IMPORTED_MODULE_2__["ClockBiometriaComponent"]
            ],
            exports: [
                _clock_biometria_component__WEBPACK_IMPORTED_MODULE_2__["ClockBiometriaComponent"]
            ],
        })
    ], ClockBiometriaModule);
    return ClockBiometriaModule;
}());



/***/ }),

/***/ "./src/app/shared/components/errors/errors.component.css":
/*!***************************************************************!*\
  !*** ./src/app/shared/components/errors/errors.component.css ***!
  \***************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = "span{\n  color: red;\n}\n\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbInNyYy9hcHAvc2hhcmVkL2NvbXBvbmVudHMvZXJyb3JzL2Vycm9ycy5jb21wb25lbnQuY3NzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiJBQUFBO0VBQ0UsVUFBVTtBQUNaIiwiZmlsZSI6InNyYy9hcHAvc2hhcmVkL2NvbXBvbmVudHMvZXJyb3JzL2Vycm9ycy5jb21wb25lbnQuY3NzIiwic291cmNlc0NvbnRlbnQiOlsic3BhbntcbiAgY29sb3I6IHJlZDtcbn1cbiJdfQ== */"

/***/ }),

/***/ "./src/app/shared/components/errors/errors.component.ts":
/*!**************************************************************!*\
  !*** ./src/app/shared/components/errors/errors.component.ts ***!
  \**************************************************************/
/*! exports provided: ErrorsComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "ErrorsComponent", function() { return ErrorsComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");


var ErrorsComponent = /** @class */ (function () {
    function ErrorsComponent() {
        this.text = '';
    }
    ErrorsComponent.prototype.ngOnInit = function () {
    };
    tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Input"])()
    ], ErrorsComponent.prototype, "text", void 0);
    ErrorsComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
            selector: 'app-errors',
            template: __webpack_require__(/*! raw-loader!./errors.component.html */ "./node_modules/raw-loader/index.js!./src/app/shared/components/errors/errors.component.html"),
            styles: [__webpack_require__(/*! ./errors.component.css */ "./src/app/shared/components/errors/errors.component.css")]
        })
    ], ErrorsComponent);
    return ErrorsComponent;
}());



/***/ }),

/***/ "./src/app/shared/components/errors/errors.module.ts":
/*!***********************************************************!*\
  !*** ./src/app/shared/components/errors/errors.module.ts ***!
  \***********************************************************/
/*! exports provided: ErrorsModule */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "ErrorsModule", function() { return ErrorsModule; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/common */ "./node_modules/@angular/common/fesm5/common.js");
/* harmony import */ var _errors_component__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./errors.component */ "./src/app/shared/components/errors/errors.component.ts");




var ErrorsModule = /** @class */ (function () {
    function ErrorsModule() {
    }
    ErrorsModule = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["NgModule"])({
            declarations: [_errors_component__WEBPACK_IMPORTED_MODULE_3__["ErrorsComponent"]],
            imports: [
                _angular_common__WEBPACK_IMPORTED_MODULE_2__["CommonModule"],
            ],
            exports: [
                _errors_component__WEBPACK_IMPORTED_MODULE_3__["ErrorsComponent"]
            ]
        })
    ], ErrorsModule);
    return ErrorsModule;
}());



/***/ }),

/***/ "./src/environments/environment.ts":
/*!*****************************************!*\
  !*** ./src/environments/environment.ts ***!
  \*****************************************/
/*! exports provided: environment */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "environment", function() { return environment; });
// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.
var environment = {
    production: false
};
/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.


/***/ }),

/***/ "./src/main.ts":
/*!*********************!*\
  !*** ./src/main.ts ***!
  \*********************/
/*! no exports provided */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_platform_browser_dynamic__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/platform-browser-dynamic */ "./node_modules/@angular/platform-browser-dynamic/fesm5/platform-browser-dynamic.js");
/* harmony import */ var _app_app_module__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./app/app.module */ "./src/app/app.module.ts");
/* harmony import */ var _environments_environment__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./environments/environment */ "./src/environments/environment.ts");




if (_environments_environment__WEBPACK_IMPORTED_MODULE_3__["environment"].production) {
    Object(_angular_core__WEBPACK_IMPORTED_MODULE_0__["enableProdMode"])();
}
Object(_angular_platform_browser_dynamic__WEBPACK_IMPORTED_MODULE_1__["platformBrowserDynamic"])().bootstrapModule(_app_app_module__WEBPACK_IMPORTED_MODULE_2__["AppModule"])
    .catch(function (err) { return console.error(err); });


/***/ }),

/***/ 0:
/*!***************************!*\
  !*** multi ./src/main.ts ***!
  \***************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__(/*! /home/henrique/Documentos/GitHub/ProjetoX/Solution/projetox-front/src/main.ts */"./src/main.ts");


/***/ })

},[[0,"runtime","vendor"]]]);
//# sourceMappingURL=main-es5.js.map