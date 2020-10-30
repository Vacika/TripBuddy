import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError, finalize, tap} from 'rxjs/operators';
import {AuthService} from "./services/auth.service";
import {Router} from "@angular/router";
import {LoaderService} from "./services/loader.service";

@Injectable()
export class CustomInterceptor implements HttpInterceptor {
	constructor(private authenticationService: AuthService,
							private loader: LoaderService,
							private router: Router) {
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
			tap((event) => this.loader.start()),
			catchError(err => {
				if (err.status === 401 || err.status === 403) {
					this.authenticationService.resetUserObservable();
					this.router.navigate(['/login'])
				}
				const error = err.error.message || err.statusText;
				this.loader.stop();
				return throwError(error);
			}),
			finalize(() => this.loader.stop()))
	}

}
