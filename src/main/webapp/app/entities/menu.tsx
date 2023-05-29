import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/search">
        <Translate contentKey="global.menu.entities.search" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/spotify-profile">
        <Translate contentKey="global.menu.entities.spotifyProfile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/music">
        <Translate contentKey="global.menu.entities.music" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/search-parameter">
        <Translate contentKey="global.menu.entities.searchParameter" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
