## 📘 Smart Savings & Insights Engine
BlackRock India Hackathon — Backend Submission

### 🚀 Overview

This project implements a complete financial computation engine for the BlackRock India Hackathon.
It processes user transactions, applies Q / P / K temporal rules, runs NPS and Index Fund projections, and outputs optimized savings insights.

All logic strictly follows the constraints and specifications defined in the hackathon document.

### 🧠 Features

✔ Transaction Parsing

- Computes ceiling (rounding to upper 100)
- Computes remanent (ceiling − amount)

✔ Transaction Validation
- Validates:
  - Duplicate timestamps
  - Invalid date format (strict yyyy-MM-dd HH:mm:ss)
  - Bounds: 0 < x < 500000
  - Consistent ceiling
  - Correct remanent
  - Non-negative remanent
  - Maximum transactions = 1,000,000

✔ Rule Engine (Q / P / K)

- Q-periods: overrides remanent (latest-start wins)
- P-periods: additive (can stack multiple extras)
- K-periods: grouping window for total savings

✔ Returns Engine
 - NPS projection (compounding)
 - Tax slab deduction modeling
 - Index Fund projection
 - Inflation adjustment

✔ Performance API

- Shows:

  - JVM memory
  - Thread count