import { ISearch } from 'app/shared/model/search.model';

export interface IMusic {
  id?: number;
  artist?: string;
  title?: string;
  album?: string;
  uri?: string;
  releasedYear?: string;
  externalUrl?: string;
  explicit?: boolean | null;
  search?: ISearch | null;
}

export const defaultValue: Readonly<IMusic> = {
  explicit: false,
};
