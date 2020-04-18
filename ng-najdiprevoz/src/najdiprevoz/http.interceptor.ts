import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {AuthService} from "./services/auth.service";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
	constructor(private authenticationService: AuthService) {
	}

	intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		console.log('test')
		return next.handle(request).pipe(catchError(err => {
			console.log('err');
			if (err.status === 401 || err.status === 403) {
				this.authenticationService.logout();
				location.reload();
			}

			const error = err.error.message || err.statusText;
			return throwError(error);
		}))
	}
}