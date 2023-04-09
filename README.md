# CustomHeroes
<h3>Классы</h3>
<ul>
<li>
  <h4>RefreshTokenRequest</h4>
  <h5>Класс <tt>RefreshTokenRequest</tt> представляет запрос на обновление токена. Содержит поле <tt>refreshToken</tt>, которое представляет собой строку, содержащую обновляемый токен.</h5>
</li>
<li>
  <h4>RefreshTokenResponse</h4>
  <h5>Класс <tt>RefreshTokenResponse</tt> представляет ответ на запрос обновления токена. Содержит поле <tt>accessToken</tt>, которое представляет собой строку, содержащую новый токен доступа.</h5>
</li>
<li>
  <h4>UserLoginRequest</h4>
  <h5>Класс <tt>UserLoginRequest</tt> представляет запрос на аутентификацию пользователя. Содержит поля <tt>username</tt> и <tt>password</tt>, которые представляют собой строки, содержащие имя пользователя и пароль соответственно.</h5>
</li>
<li>
  <h4>UserRegistrationRequest</h4>
  <h5>Класс <tt>UserRegistrationRequest</tt> представляет запрос на регистрацию нового пользователя. Содержит поля <tt>username</tt> и <tt>password</tt>, которые представляют собой строки, содержащие желаемое имя пользователя и пароль соответственно.</h5>
</li>
</ul>
<hr>
<h3>API endpoints</h3>
<ul>
<li>
  <h4>POST /api/auth/login</h4>
  <h5>API endpoint <tt>/api/auth/login</tt> предназначен для аутентификации пользователя. Принимает объект <tt>UserLoginRequest</tt> в теле запроса. Возвращает ResponseEntity с телом в виде JwtResponse, содержащего токен доступа и токен обновления.</h5>
</li>
<li>
  <h4>POST /api/auth/register</h4>
  <h5>API endpoint <tt>/api/auth/register</tt> предназначен для регистрации нового пользователя. Принимает объект <tt>UserRegistrationRequest</tt> в теле запроса. Возвращает ResponseEntity со статусом HttpStatus.CREATED.</h5>
</li>
<li>
  <h4>POST /api/auth/logout</h4>
  <h5>API endpoint <tt>/api/auth/logout</tt> предназначен для выхода пользователя из системы. Принимает заголовок <tt>Authorization</tt>, содержащий токен доступа. Возвращает ResponseEntity со статусом HttpStatus.OK.</h5>
</li>
<li>
  <h4>POST /api/auth/refresh</h4>
  <h5>API endpoint <tt>/api/auth/refresh</tt> предназначен для обновления токена доступа. Принимает объект <tt>RefreshTokenRequest</tt> в теле запроса. Возвращает ResponseEntity с телом в виде RefreshTokenResponse, содержащего новый токен доступа.</h5>
</li>
</ul>
