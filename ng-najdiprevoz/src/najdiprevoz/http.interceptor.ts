import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {AuthService} from "./services/auth.service";
import {Router} from "@angular/router";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
	constructor(private authenticationService: AuthService,
							private router: Router) {
	}

	intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		return next.handle(request).pipe(catchError(err => {
			console.log('err');
			if (err.status === 401 || err.status === 403) {
				this.authenticationService.resetUserObservable();
				this.router.navigate(['/login'])
			}

			const error = err.error.message || err.statusText;
			return throwError(error);
		}))
	}
}