/**
 * These files fulfil the PageObject API design pattern :
 * http://martinfowler.com/bliki/PageObject.html
 *
 * Outwardly they presents a simple api that tests can be written against.
 * Inwardly they encapsulate all the page implementation specific and layout specific code.
 *
 * The benefit of this is that changes to the layout of the page can be managed entirely by the PageObject,
 * without having to change the tests themselves, this promotes code reuse and eases maintenance.
 *
 */