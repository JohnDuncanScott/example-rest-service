export const CURRENCY_CODE_KEY = "CURRENCY_CODE";

class UserPersonalisationService {
    settings = {};

    storeValue(key, value) {
        console.log(`Storing key: ${key}, value: ${value}`)
        this.settings[key] = value;
    }

    getValue(key) {
        var value = this.settings[key];
        console.log(`Getting value for ${key}, value: ${value}`)
        return this.settings[key];
    }
}

export default new UserPersonalisationService();