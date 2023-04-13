<html lang="en">
    <head>
        <title>Medic Registration</title>
        <link href="/css/register_medic.css" rel="stylesheet"/>
    </head>

    <body>
        <h1>REGISTER MEDIC</h1>
        <c:url value="/register_medic" var="registerMedicUrl" />
        <form class="form-container" action="${register_medic}" method="POST">
            <div class="form-item">
                <label for="name">Name</label>
                <input type="text" name="name" placeholder="Juan" id="name" required/>
            </div>
            <div class="form-item">
                <label for="lastname">Lastname</label>
                <input id="lastname"type="text" name="lastname" placeholder="Bafico" required/>
            </div>
            <div class="form-item">
                <div>
                    <label for="city">City</label>
                    <input id="city" type="text" name="city" placeholder="CABA" required/>
                </div>
                <div>
                    <label for="address">Address</label>
                    <input id="address" type="text" name="address" placeholder="Libertador 1234" required/>
                </div>
            </div>
            <!-- dropdown menu -->
            <div class="form-item">
                <label for="specialization">Specialization</label>
                <input id="specialization" type="text" name="specialization" required/>
            </div>

            <!-- multiple option buttons -->
            <!-- TODO: buscar otro nombre para obra social -->
            <div class="form-item" >
                <label for="obra_social">Obra Social</label>
                <input id="obra_social" type="text" name="obra_social" required/>
            </div>
            <div class="form-item">
                <label for="email">Email</label>
                <input id="email" type="text" name="email" placeholder="juan.bafico@email.com" required/>
            </div>
            <div class="form-item">
                <label for="password">Password</label>
                <input id="password" type="password" name="password" required/>
            </div>
            <div class="form-item">
                <label for="confirmpassword">Confirm Password</label>
                <input id="confirmpassword" type="password" name="confirm_password" required/>
            </div>
            <button type="submit">Register</button>
        </form>
    </body>
</html>
