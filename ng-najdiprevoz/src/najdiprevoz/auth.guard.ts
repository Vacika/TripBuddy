import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {UserService} from "./services/user.service";

@Injectable({providedIn: 'root'})
export class AuthGuard implements CanActivate {
	constructor(
		private router: Router,
		private _userService: UserService) {
	}

	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
		const loggedUser = this._userService.isUserLoggedIn();
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