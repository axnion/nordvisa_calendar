import React, { Component } from 'react';
import PropTypes from 'prop-types';
import './MyEventsView.css';
import EventsList from './EventsList';
import Client from '../Client';
import ConfirmMessage from './ConfirmMessage';

class MyEventsView extends Component {
  constructor(props) {
    super(props);

    this.handleDeleteClick = this.handleDeleteClick.bind(this);
    this.onYesClick = this.onYesClick.bind(this);
  }
  state = {
    events: [],
    toDelete: null,
    popup: {
      pop: false,
      msg: '',
    },
  }


  componentWillMount() {
    const uri = '/api/event/get_manageable';
    Client.get(uri)
      .then((events) => {
        if (Array.isArray(events)) {
          this.setState({ events });
        }
      });
  }


  onYesClick() {
    const uri = '/api/event/delete';
    const eventToDelete = {
      id: this.state.toDelete,
    };

    Client.post(eventToDelete, uri);

    const events = this.state.events.filter(event => event.id !== this.state.toDelete);
    this.setState({ events });
  }

  handleDeleteClick(evt) {
    evt.preventDefault();
    const eventName = this.state.events.filter((event) => {
      if (event.id === evt.target.name) {
        return event.name;
      }
      return '';
    })[0];

    const popup = {
      pop: true,
      msg: `Are you sure you want to delete the event ${eventName.name}?`,
    };
    this.setState({ popup });
    this.setState({ toDelete: eventName.id });
  }

  render() {
    const language = this.context.language.MyEventsView;

    return (
      <div className="view">
        <h2 className="capitalize">{language.myEvents}</h2>
        <EventsList events={this.state.events} delete={this.handleDeleteClick} />
        <ConfirmMessage popup={this.state.popup} onClick={this.onYesClick} />
      </div>
    );
  }
}

MyEventsView.contextTypes = {
  language: PropTypes.object,
};

export default MyEventsView;