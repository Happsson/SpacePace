package nu.geeks.spacepace;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class SpacePace extends ApplicationAdapter {

    private OrthographicCamera cam;
    Body body;
    private World world;
    private Box2DDebugRenderer render;

	@Override
	public void create () {

        cam = new OrthographicCamera(Gdx.graphics.getWidth() / 70, Gdx.graphics.getHeight() / 70);
        render = new Box2DDebugRenderer();
        world = new World(new Vector2(0,0), true);

        BodyDef bdef = new BodyDef();
        bdef.position.set(0,0);
        bdef.type = BodyDef.BodyType.DynamicBody;


        body = world.createBody(bdef);
        CircleShape shape = new CircleShape();
        shape.setRadius(50f);

        FixtureDef fdef =  new FixtureDef();
        fdef.shape = shape;
        fdef.density = 100f;

        body.createFixture(fdef);

        bdef.position.set(0,60);
        bdef.type = BodyDef.BodyType.DynamicBody;

        shape.setRadius(10f);
        body = world.createBody(bdef);

        fdef.friction = .3f;
        fdef.restitution = .3f;
        fdef.density = 20f;


        body.createFixture(fdef);



        Gdx.input.setInputProcessor(new InputMultiplexer(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {


                switch(keycode) {
                    case Input.Keys.SPACE:
                        body.applyForceToCenter(8E8f, 2E7f,true);
                        break;

                    case Input.Keys.W:
                        body.applyForceToCenter(0, 5000000f, true);
                        break;
                    case Input.Keys.S:
                        body.applyForceToCenter(0, -5000000f, true);
                        break;
                    case Input.Keys.D:
                        body.applyForceToCenter(5000000, 0, true);
                        break;
                    case Input.Keys.A:
                        body.applyForceToCenter(-5000000, 0, true);
                        break;
                }
                return true;
            }
            @Override
            public boolean touchDown (int screenX, int screenY, int pointer, int button) {

                return true;
            }
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer,
                                   int button) {
                return true;
            }
        }));

        cam.zoom = 50f;
        cam.update();

    }




    private Vector2 calculateForces(Body body){
        Array<Body> bodies  = new Array<Body>();
        world.getBodies(bodies);
        Vector2 forces = new Vector2();
        for(Body b : bodies){

            //Create new Vector2. Subtract the position of the body we are calculation for from the current body b.
            Vector2 pos = new Vector2(
                    b.getPosition().x - body.getPosition().x,
                    b.getPosition().y- body.getPosition().y);
            if(pos.len() > 0) {
                //Force = G * (m1 * m2)/r^2
                double force = 0.0067 *  (body.getMass() * b.getMass() / (pos.len() * pos.len()));
                //System.out.println("body.m: " + body.getMass() + " b.m: " + b.getMass() + " pos.len: " + pos.len() );
                pos.x *= force;
                pos.y *= force;
                forces.add(pos);
            }
        }

        return forces;
    }



	@Override
	public void render () {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        body.applyForceToCenter(calculateForces(body), true);

        world.step(0.02f, 8, 3);
        render.render(world, cam.combined);

	}
}
