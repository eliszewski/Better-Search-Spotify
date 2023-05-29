import { ISearch } from 'app/shared/model/search.model';
import { SearchParameterType } from 'app/shared/model/enumerations/search-parameter-type.model';
import { SearchParameterAttribute } from 'app/shared/model/enumerations/search-parameter-attribute.model';
import { AlbumType } from 'app/shared/model/enumerations/album-type.model';

export interface ISearchParameter {
  id?: number;
  type?: SearchParameterType | null;
  attributeName?: SearchParameterAttribute | null;
  albumType?: AlbumType | null;
  searchValue?: string | null;
  searches?: ISearch[] | null;
}

export const defaultValue: Readonly<ISearchParameter> = {};
