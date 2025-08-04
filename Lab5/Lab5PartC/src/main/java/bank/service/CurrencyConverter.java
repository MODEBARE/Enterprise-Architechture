package bank.service;

import bank.logging.ILogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CurrencyConverter implements ICurrencyConverter {

    private final ILogger logger;

    @Autowired
    public CurrencyConverter(ILogger logger) {
        this.logger = logger;
    }

    public double euroToDollars(double amount) {
        double converted = 1.57 * amount;
        logger.log("CurrencyConverter: converting " + amount + " euros to " + converted + " dollars");
        return converted;
    }

    public double dollarsToEuros(double amount) {
        double converted = 0.637 * amount;
        logger.log("CurrencyConverter: converting " + amount + " dollars to " + converted + " euros");
        return converted;
    }
}
