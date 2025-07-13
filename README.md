# Estro-Tracker

## What is it???
This is our Android Studio app that will help track estrogen doses for feminizing HRT.

It is our entry for the UAH 2025 ACM Hackathon.

## How can I use it?
If you have an android device, then check the releases page!

## Features
What all can it do? 

This project is, of course, a work in progress. 
Right now, this is our plan forwards on what we will be doing:

### Base Features
- [x] Graphs
  - A graph of the expected estrogen levels for the time following each dosage.
- [x] Logging
  - A function to log when one takes their dose to update the graph.
- [x] Enum for Med Types
  - Storage of the data for each medication type, basically expanding the app to be able to use more meds.

### Extra Enhancements
- [x] Log autofill
  - Adds an option to automatically fill in the data for the meds based what day it is and some defaults.
- [x] Concentration to mg Calculator
  - A calculator that will automatically calculate the correct dosage based on the concentration of the meds.
- [x] Dark Mode Settings
  - Added customizability to make the app dark mode and light mode
  - Not all screens supported yet
- [ ] Encryption
  - While the health data does not get sent back to a different server and is all stored locally, it is still a good idea to encrypt it for security.
- [ ] Bloodwork
  - Let the user put in bloodwork levels from labs they've had performed. Will appear on the chart with the predicted levels for reference.
- [ ] Extra Settings
  - Add extra settings for the graph's view and other things.
- [ ] Dose Notifications
  - A notification to remind users to take their dose.

---
### Disclaimer!
This app is a general guide when it comes to estrogen levels. Don't rely on it for personal hormone levels, use blood work for that!

Estresso, derived from [estrannaise](https://estrannai.se/), is designed to give you an idea of what your levels are and show you where you are in your cycle. Estrogen levels vary greatly between individuals, so take these levels with a grain of salt!

Obligatory MIT license from estrannaise and for Hackathon :)

