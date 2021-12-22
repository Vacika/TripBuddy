import {Subject} from "rxjs";
import {Injectable} from "@angular/core";
import {LoaderState} from "../../interfaces/loader-state.interface";

@Injectable({
	providedIn: 'root'
})
export class LoaderService {
	private loaderSubject = new Subject<LoaderState>();
	loaderState = this.loaderSubject.asObservable();

	constructor() {
	}

	start() {
		this.loaderSubject.next(<LoaderState>{show: true});
	}

	stop() {
		this.loaderSubject.next(<LoaderState>{show: false});
	}
}