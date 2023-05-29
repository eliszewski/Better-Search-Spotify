import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface ISpotifyProfile {
  id?: number;
  accessToken?: string | null;
  refreshToken?: string | null;
  accessTokenExpiration?: string | null;
  refreshTokenExpiration?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<ISpotifyProfile> = {};
