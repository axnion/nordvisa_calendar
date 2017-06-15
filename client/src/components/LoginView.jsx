import React, { Component } from 'react';
import Link from 'react-router/Link';
import PropTypes from 'prop-types';
import { isEmail } from 'validator';
import Redirect from 'react-router/Redirect';
import './LoginView.css';
import ErrorList from './ErrorList';
import Client from '../Client';
import Loader from './Loader';


class LoginView extends Component {
  state = {
    fields: {
      email: '',
      password: '',

    },
    fieldErrors: [],
    loginInProgress: false,
    shouldRedirect: false,
  }


  componentWillMount() {
    const fieldErrors = this.state.fieldErrors;

    if (this.props.location.state) {
      if (this.props.location.state.referrer === '/register') {
        fieldErrors.push(this.context.language.LoginView.registration);
      }
      if (this.props.location.state.referrer === '/recover-password') {
        fieldErrors.push(this.context.language.LoginView.recover);
      }
    }

    this.setState(fieldErrors);
  }

  performLogin(user) {
    this.setState({ loginInProgress: true });
    const uri = '/login';
    Client.login(user, uri).then((res) => {
      if (res === 'success') {
        this.setState({ shouldRedirect: true });
        this.forceUpdate();
      } else {
        const fieldErrors = [this.context.language.Errors.loginFailed];
        this.setState({ loginInProgress: false, fieldErrors });
      }
    });
  }

  validate(fields) {
    const errors = [];
    if (!isEmail(fields.email)) errors.push(this.context.language.Errors.invalidEmail);
    if (!fields.password) errors.push(this.context.language.Errors.emptyPassword);
    if (fields.password.length < 10) errors.push(this.context.language.Errors.incorrectPassword);
    if (fields.password.length > 255) errors.push(this.context.language.Errors.incorrectPassword);
    return errors;
  }

  onFormSubmit(event) {
    event.preventDefault();

    const fieldErrors = this.validate(this.state.fields);
    this.setState({ fieldErrors });

    // Return on Errors
    if (fieldErrors.length) return;

    const user = {
      username: this.state.fields.email,
      password: this.state.fields.password,
    };

    // TODO Might change
    this.performLogin(user);
    this.setState({ fields: { email: '', password: '' } });
  }

  onInputChange(event) {
    const fields = this.state.fields;
    fields[event.target.name] = event.target.value;
    this.setState({ fields });
  }
  render() {
    const language = this.context.language;

    if (this.state.shouldRedirect) {
      return (
        <Redirect to="/user/event" />
      );
    } else if (this.state.loginInProgress) {
      return (
        <Loader />
      );
    }
    return (
      <div className="lightbox login">
        <h2 className="uppercase">{language.LoginView.login}</h2>
        <form onSubmit={this.onFormSubmit.bind(this)}>
          <label htmlFor="email" className="capitalize">{language.LoginView.email}:</label>
          <input
            name="email"
            value={this.state.fields.email}
            onChange={this.onInputChange.bind(this)}
            type="text"
          />
          <label htmlFor="password" className="capitalize">{language.LoginView.password}:</label>
          <input
            name="password"
            onChange={this.onInputChange.bind(this)}
            value={this.state.fields.password}
            type="password"
          />
          <ErrorList errors={this.state.fieldErrors} />
          <input type="submit" className="btn-primary" value={language.LoginView.login} />
        </form>
        <Link to="/recover-password" className="capitalize">{language.LoginView.forgot}</Link>
      </div>
    );
  }
}

LoginView.contextTypes = {
  language: PropTypes.object,
};

export default LoginView;
