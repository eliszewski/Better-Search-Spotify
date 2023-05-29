import search from 'app/entities/search/search.reducer';
import spotifyProfile from 'app/entities/spotify-profile/spotify-profile.reducer';
import music from 'app/entities/music/music.reducer';
import searchParameter from 'app/entities/search-parameter/search-parameter.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  search,
  spotifyProfile,
  music,
  searchParameter,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
