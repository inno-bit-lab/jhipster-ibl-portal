import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Utente e2e test', () => {
  const utentePageUrl = '/utente';
  const utentePageUrlPattern = new RegExp('/utente(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const utenteSample = {};

  let utente;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/utentes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/utentes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/utentes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (utente) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/utentes/${utente.id}`,
      }).then(() => {
        utente = undefined;
      });
    }
  });

  it('Utentes menu should load Utentes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('utente');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Utente').should('exist');
    cy.url().should('match', utentePageUrlPattern);
  });

  describe('Utente page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(utentePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Utente page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/utente/new$'));
        cy.getEntityCreateUpdateHeading('Utente');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', utentePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/utentes',
          body: utenteSample,
        }).then(({ body }) => {
          utente = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/utentes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [utente],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(utentePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Utente page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('utente');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', utentePageUrlPattern);
      });

      it('edit button click should load edit Utente page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Utente');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', utentePageUrlPattern);
      });

      it('edit button click should load edit Utente page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Utente');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', utentePageUrlPattern);
      });

      it('last delete button click should delete instance of Utente', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('utente').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', utentePageUrlPattern);

        utente = undefined;
      });
    });
  });

  describe('new Utente page', () => {
    beforeEach(() => {
      cy.visit(`${utentePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Utente');
    });

    it('should create an instance of Utente', () => {
      cy.get(`[data-cy="created"]`).type('2024-02-24');
      cy.get(`[data-cy="created"]`).blur();
      cy.get(`[data-cy="created"]`).should('have.value', '2024-02-24');

      cy.get(`[data-cy="modified"]`).type('2024-02-24');
      cy.get(`[data-cy="modified"]`).blur();
      cy.get(`[data-cy="modified"]`).should('have.value', '2024-02-24');

      cy.get(`[data-cy="username"]`).type('landmine er');
      cy.get(`[data-cy="username"]`).should('have.value', 'landmine er');

      cy.get(`[data-cy="password"]`).type('unfortunately edify ruddy');
      cy.get(`[data-cy="password"]`).should('have.value', 'unfortunately edify ruddy');

      cy.get(`[data-cy="mail"]`).type('emerald');
      cy.get(`[data-cy="mail"]`).should('have.value', 'emerald');

      cy.get(`[data-cy="mobile"]`).type('psst quarrelsome');
      cy.get(`[data-cy="mobile"]`).should('have.value', 'psst quarrelsome');

      cy.get(`[data-cy="facebook"]`).type('likely bah');
      cy.get(`[data-cy="facebook"]`).should('have.value', 'likely bah');

      cy.get(`[data-cy="google"]`).type('instead even hot');
      cy.get(`[data-cy="google"]`).should('have.value', 'instead even hot');

      cy.get(`[data-cy="instangram"]`).type('full even entreat');
      cy.get(`[data-cy="instangram"]`).should('have.value', 'full even entreat');

      cy.get(`[data-cy="provider"]`).type('offbeat');
      cy.get(`[data-cy="provider"]`).should('have.value', 'offbeat');

      cy.get(`[data-cy="attivo"]`).should('not.be.checked');
      cy.get(`[data-cy="attivo"]`).click();
      cy.get(`[data-cy="attivo"]`).should('be.checked');

      cy.get(`[data-cy="motivoBolocco"]`).type('urgently');
      cy.get(`[data-cy="motivoBolocco"]`).should('have.value', 'urgently');

      cy.get(`[data-cy="dataBolocco"]`).type('2024-02-24');
      cy.get(`[data-cy="dataBolocco"]`).blur();
      cy.get(`[data-cy="dataBolocco"]`).should('have.value', '2024-02-24');

      cy.get(`[data-cy="registrationDate"]`).type('2024-02-23');
      cy.get(`[data-cy="registrationDate"]`).blur();
      cy.get(`[data-cy="registrationDate"]`).should('have.value', '2024-02-23');

      cy.get(`[data-cy="lastAccess"]`).type('2024-02-24');
      cy.get(`[data-cy="lastAccess"]`).blur();
      cy.get(`[data-cy="lastAccess"]`).should('have.value', '2024-02-24');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        utente = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', utentePageUrlPattern);
    });
  });
});
