import React from "react";
import LoginForm from "./components/LoginForm";
import Footer from "./components/Footer";
import SignupForm from "./components/SignupForm";
import { Layout, Dropdown, Menu, Button } from "antd";
import { UserOutlined } from "@ant-design/icons";

const { Header, Content } = Layout;

class App extends React.Component {
  state = {
    authed: false,
    asNGO: false,
  };

  componentDidMount() {
    //check the existence of token in local storage
    const authToken = localStorage.getItem("authToken");
    const asNGO = localStorage.getItem("asNGO") === "true";
    this.setState({
      authed: authToken !== null,
      asNGO,
    });
  }

  renderLoginContent = () => {
    if (!this.state.authed) {
      return <LoginForm handleLoginSucces={this.handleLoginSucces} />;
    }

    if (this.state.asNGO) {
      return <div>NGO page</div>;
    }

    return <div>Donator page</div>;
  };

  handleLoginSucces = (token, asNGO) => {
    localStorage.setItem("authToken", token);
    localStorage.setItem("asNGO", asNGO);
    this.setState({ authed: true, asNGO });
  };
  handleLogOut = () => {
    localStorage.removeItem("authToken");
    localStorage.removeItem("asNGO");
    this.setState({
      authed: false,
    });
  }

  userMenu = (
    <Menu>
      <Menu.Item key="logout" onClick={this.handleLogOut}>
        Log Out
      </Menu.Item>
    </Menu>
  );
  render() {
    let logoUrl =
      "https://lh3.googleusercontent.com/kR336RQiLl2T5YPjI24kCQUP8FfsSkCY0CVoMsPpC62hBam0qHQ-qyKtcsmlaxep2FiFKgE9mkw9axkxCIKa_Xt0JDNMEbzTACuiE9YXYzPdaNPYQB65os9dfPjY7RH2kbVikD7Vkw=w2400";
    return (
      <Layout style={{ height: "100vh" }}>
       <Header style={{ display: "flex", justifyContent: "space-between" }}>
            <div className="slogan">
              <img src={logoUrl} className="logo" height={70} width={80} alt="placeholder"/>
              <div className="title">Donations</div>
            </div>
            {this.state.authed && (
              <div>
              <Dropdown trigger="click" overlay={this.userMenu}>
                <Button icon={<UserOutlined />} shape="circle" />
              </Dropdown>
            </div>
            )}
          
        </Header>
        <Content style={{ height: "calc(100% - 64px)", margin: 20, overflow: "auto" }}
        >
          {this.renderContent()}
        </Content>
      </Layout>
    );
  }
}


export default App;
