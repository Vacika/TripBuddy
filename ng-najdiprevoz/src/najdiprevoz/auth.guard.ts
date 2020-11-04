import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {AuthService} from "./services/auth.service";

@Injectable({providedIn: 'root'})
export class AuthGuard implements CanActivate {
	constructor(
		private router: Router,
		private authenticationService: AuthService) {
	}

	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
		const loggedUser = this.authenticationService.isUserLoggedIn();
		if (loggedUser) {
			// logged in so return true
			return true;
		}
		console.log("Not logged in!");
		// not logged in so redirect to login page with the return url
		this.router.navigate(['/login'], {queryParams: {returnUrl: state.url}});
		return false;
	}
}