import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {AuthService} from "./services/auth.service";

@Injectable({providedIn: 'root'})
export class AdminAuthGuard implements CanActivate {
	constructor(
		private router: Router,
		private authenticationService: AuthService) {
	}

	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
		const currentUser = this.authenticationService.getLoggedUser();
		if (currentUser && currentUser.authorities[0].authority === 'ROLE_ADMIN') {
			// logged in so return true
			return true;
		}
		// not logged in so redirect to login page with the return url
		this.router.navigate(['/']);
		return false;
	}
}