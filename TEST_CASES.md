# Manual Test Cases

| ID | Test | Expected result |
|---|---|---|
| TC-01 | Open market menu | Six demo stocks and prices appear |
| TC-02 | Buy 2 WIPRO shares with sufficient cash | Purchase succeeds; cash decreases by ₹1,040 |
| TC-03 | Buy using `UNKNOWN` symbol | Clear stock-not-found message |
| TC-04 | Enter quantity `0` or a negative number | Trade rejected |
| TC-05 | Buy shares costing more than available cash | Insufficient-funds message; no trade |
| TC-06 | Sell 1 WIPRO share when at least 1 is owned | Sale succeeds; cash increases by ₹520 |
| TC-07 | Sell more shares than owned | Sale rejected; holdings unchanged |
| TC-08 | Sell a stock not held | Clear message; no trade |
| TC-09 | Enter letters at numeric prompt | Program asks for a valid number |
| TC-10 | View transaction history after trades | BUY/SELL entries with timestamp appear |
| TC-11 | Exit and restart | Cash balance and holdings persist |
| TC-12 | Add a positive amount of demo funds | Balance increases by that amount |

**Note:** All values are based on fixed demo prices and no brokerage/tax is applied.
