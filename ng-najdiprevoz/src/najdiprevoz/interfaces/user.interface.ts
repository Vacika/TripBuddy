export interface User{
  id: number,
  firstName: string
  lastName: string
  username: string
  profilePhoto?: string
  phoneNumber?: string
  gender: string
  averageRating: number,
	birthDate: string
	password?: string
	defaultLanguage: string;
}
