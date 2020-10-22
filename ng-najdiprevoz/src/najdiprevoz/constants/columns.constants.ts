export const adminTableColumns = [
	'username',
	'birthDate',
	'phoneNumber',
	'firstName',
	'lastName',
	'defaultLanguage',
	'registeredOn',
	'isActivated',
	'isBanned',
	'adminActions']


export const tableColumnsForSent = [
	"allowedActions",
	"fromLocation",
	"toLocation",
	"departureTime",
	"requestStatus",
	"driverName",
	"tripId",
	"rideStatus"
];

export const tableColumnsForReceived = [
	"allowedActions",
	"requesterName",
	"fromLocation",
	"toLocation",
	// "requestedSeats",
	"departureTime",
	"requestStatus",
	"tripId",
	"rideStatus"
];

export const tableColumnsAsDriver = [
	'from',
	'to',
	'departureTime',
	'totalSeats',
	'pricePerHead',
	'status',
	'allowedActions'
];

export const tableColumnsAsPassenger = [
	'from',
	'to',
	'departureTime',
	'totalSeats',
	'pricePerHead',
	'driver',
	'status'
];