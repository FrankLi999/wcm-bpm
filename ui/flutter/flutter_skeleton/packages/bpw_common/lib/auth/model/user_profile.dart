class UserLogin {
  String email;
  String password;

  UserLogin({this.email, this.password});

  Map<String, dynamic> toJson() =>
      {"email": this.email, "password": this.password};
}

class User {
  User();
  String id;
  String name;
  String firstName;
  String lastName;
  String email;
  String imageUrl;
  String accessToken;
  String sessionId;
  int expireIn;
  List<String> roles;
  String tokenType;

  factory User.fromJson(Map<String, dynamic> json) {
    User user = User();
    user.id = json['id'];
    user.email = json['email'];
    user.name = json['name'];
    user.roles = json['roles'] != null ? List.from(json['roles']) : null;
    user.accessToken = json['accessToken'];
    user.tokenType = json['tokenType'];
    user.expireIn = json['expireIn'] as int;
    user.sessionId = json['sessionId'];
    return user;
  }
}
