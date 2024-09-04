console.log("Script loaded");

let currentTheme = getTheme();

document.addEventListener("DOMContentLoaded", () => {
  changeTheme();
});

//todo:
function changeTheme() {
  //set to web page
  ChangePageTheme(currentTheme, currentTheme);
  document.querySelector("html").classList.add(currentTheme);

  //set the listener to change button
  const changeThemeButton = document.querySelector("#theme_change_button");

  changeThemeButton.addEventListener("click", () => {
    let oldTheme = currentTheme;
    console.log("button clicked");

    if (currentTheme == "dark") {
      //theme change to light
      currentTheme = "light";
    } else {
      //changer to dark
      currentTheme = "dark";
    }
    ChangePageTheme(currentTheme, oldTheme);
  });
}

//set theme to local storage
function setTheme(theme) {
  localStorage.setItem("theme", theme);
}
//get theme from local storage
function getTheme() {
  let theme = localStorage.getItem("theme");
  return theme ? theme : "Light";
}

//change curuunt page theme

function ChangePageTheme(theme, oldTheme) {
  //localstorage me uodate
  setTheme(currentTheme);
  //remove currunt theme
  if(oldTheme){
  document.querySelector("html").classList.remove(oldTheme);
}
  //set currunt theme
  document.querySelector("html").classList.add(theme);

  //chage the text of theme button
  document
    .querySelector("#theme_change_button")
    .querySelector("span").textContent = theme == "light" ? "Dark" : "Light";
}
//end of page theme work
