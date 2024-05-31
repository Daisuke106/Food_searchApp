// userStorage.js

const getCurrentUser = () => {
    try {
        const user = localStorage.getItem("user");
        return user ? JSON.parse(user) : null;
    } catch (error) {
        console.error("Error parsing user from localStorage:", error);
        return null;
    }
};

const setCurrentUser = (response) => {
    try {
        localStorage.setItem("user", JSON.stringify(response));
    } catch (error) {
        console.error("Error setting user to localStorage:", error);
    }
};

const removeCurrentUser = () => {
    try {
        localStorage.removeItem("user");
    } catch (error) {
        console.error("Error removing user from localStorage:", error);
    }
}

const userStorage = {
    setCurrentUser,
    getCurrentUser,
    removeCurrentUser
};

export default userStorage;
