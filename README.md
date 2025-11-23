# PROMAN

PROMAN is a web application designed to help property owners and managers efficiently track rent payments, maintain financial records, and analyze the performance of their properties.

---

## Features

- **Tenant Management**: Add, update, and manage tenant information.  
- **Rent Tracking**: Record and track rent payments with due dates and reminders.    
- **Property Performance Dashboard**: Visualize property performance over time with charts and summaries.  
- **Reports**: Generate detailed reports for individual properties or overall portfolio performance.  

---

## TechStack

- **Frontend**: HTML, CSS, JavaScript 
- **Backend**: Java (Javalin)  
- **Database**: PostgreSQL
- **Quartz API** Implementes Automation with Qaurtz API Jobs to send reminders to users and tracks overdue day of tenants
---

## Project_Overview

1. **Sign up **  

sign_up_session flow-part1.png
![Alt text](System_design/proman.drawio.png)
- Setting the databse model for new user.
- **ProMan REQUIRES each user(property_owner) to provide information about they rental properties**

## Business Logic(Reminders and tracking tenant payemnts)
3. The functionality of the business depends on QUARTZ APIS<JOBS> 
   - A Quartz Job is a unit of work that Quartz executes at a scheduled time.
   - It acts like a small program inside the application that runs automatically based on a Cron expression or trigger.
   - Each job contains the logic for a specific automated task.
 
   - PROMAN  implements Email Notification Job and Tenant Overdue Tracking Job
   - **The Overall design of the business Logic**   
![Alt text](System_design/automation.drawio.png)
  


