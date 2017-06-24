import React from 'react';
import PropTypes from 'prop-types';

const DenmarkSelect = ({ onChange, showProvince }, context) => {
  const language = context.language.WidgetView;

  return (
    <div className={showProvince ? '' : 'hidden'}>
      <select defaultValue="" name="province" onChange={onChange} className="capitalize">
        <option value="" className="capitalize">{language.chooseProvince}</option>
        <option value="" className="capitalize">{language.none}</option>
        <option value="nordjylland">Nordjylland</option>
        <option value="midtjylland">Midtjylland</option>
        <option value="syddanmark">Syddanmark</option>
        <option value="hovedstaden">Hovedstaden</option>
        <option value="sjaelland">Sjælland</option>
      </select>
    </div>
  );
};

DenmarkSelect.contextTypes = {
  language: PropTypes.object,
};

DenmarkSelect.defaultProps = {
  showProvince: false,
};

DenmarkSelect.propTypes = {
  showProvince: PropTypes.boolean,
  onChange: PropTypes.func.isRequired,
};
