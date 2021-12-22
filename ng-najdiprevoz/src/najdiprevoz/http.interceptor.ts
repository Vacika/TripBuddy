import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError, finalize, tap} from 'rxjs/operators';
import {Router} from "@angular/router";
import {LoaderService} from "./services/util/loader.service";
import {UserService} from "./services/user.service";

@Injectable()
export class CustomInterceptor implements HttpInterceptor {
	constructor(private _userService: UserService,
							private _loader: LoaderService,
							private _router: Router) {
	}

	intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		if (sessionStorage.getItem('currentUser') && sessionStorage.getItem('token')) {
			request = request.clone({
				setHeaders: {
					Authorization: sessionStorage.getItem('token')
				}
			})
		}

		return next.handle(request).pipe(
			tap((event) => this._loader.start()),
			catchError(err => {
				console.log("ERROR", err);
				if (err.status === 401 || err.status === 403) {
					this._userService.resetUserObservable();
					this._router.navigate(['/login'])
				}
				const error = err.error.message || err.statusText;
				this._loader.stop();
				return throwError(error);
			}),
			finalize(() => this._loader.stop()))
	}

}
