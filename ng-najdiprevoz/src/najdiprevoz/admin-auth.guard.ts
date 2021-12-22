import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {UserService} from "./services/user.service";

@Injectable({providedIn: 'root'})
export class AdminAuthGuard implements CanActivate {
	constructor(
		private router: Router,
		private userService: UserService) {
	}

	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
		const currentUser = this.userService.getLoggedUser();
		if (currentUser && currentUser.authorities[0].authority === 'ROLE_ADMIN') {
			// logged in so return true
			return true;
		}
		// not logged in so redirect to login page with the return url
		this.router.navigate(['/']);
		return false;
	}
}