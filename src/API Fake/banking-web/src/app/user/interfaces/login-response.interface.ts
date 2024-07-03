export interface LoginResponse {
    message: string;
    user: {
      id: number;
      firstName: string;
      lastName: string;
      email: string;
      //password: string; 
    };
  }
  