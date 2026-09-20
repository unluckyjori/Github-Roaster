const form = document.getElementById("roast-form");
const usernameInput = document.getElementById("username");
const result = document.getElementById("result");

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const username = usernameInput.value.trim();
    result.textContent = "Preparing your roast...";

    try {
        const response = await fetch(
            "/roast?username=" + encodeURIComponent(username)
        );

        if (!response.ok) {
            throw new Error("Could not roast this profile.");
        }

        const roast = await response.text();
        result.textContent = roast;
    } catch (error) {
        result.textContent = error.message;
    }
});