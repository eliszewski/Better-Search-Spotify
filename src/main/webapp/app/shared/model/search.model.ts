import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ISearchParameter } from 'app/shared/model/search-parameter.model';
import { IMusic } from 'app/shared/model/music.model';

export interface ISearch {
  id?: number;
  name?: string;
  date?: string;
  createPlayist?: boolean;
  user?: IUser | null;
  searchParameter?: ISearchParameter | null;
  music?: IMusic[] | null;
}

export const defaultValue: Readonly<ISearch> = {
  createPlayist: false,
};
